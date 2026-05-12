import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import SearchDropdown from './SearchDropdown';
import logoImg from '../assets/images/logo.png';

function Navbar() {
  const [showDropdown, setShowDropdown] = useState(false);
  const navigate = useNavigate();

  return (
    <nav className="flex items-center justify-between px-8 py-4 bg-transparent relative z-50">

      {/* Left - Logo Image */}
      <div
        className="cursor-pointer"
        onClick={() => navigate('/home')}
      >
        <img
          src={logoImg}
          alt="Tanishq"
          className="h-12 w-auto"
        />
      </div>

      {/* Center - Search Bar */}
      <div className="relative w-2/5">
        <div
          className="flex items-center bg-[#c8d5b9] rounded-full px-4 py-2 cursor-text"
          onClick={() => setShowDropdown(true)}
        >
          <svg className="w-5 h-5 text-gray-600 mr-2 flex-shrink-0"
            fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
              d="M21 21l-4.35-4.35M17 11A6 6 0 1 1 5 11a6 6 0 0 1 12 0z"/>
          </svg>
          <span className="text-gray-500 text-sm font-montserrat">
            Search Products
          </span>
        </div>

        {showDropdown && (
          <SearchDropdown
            onSelect={(category) => {
              setShowDropdown(false);
              navigate(`/category/${category}`);
            }}
            onClose={() => setShowDropdown(false)}
          />
        )}
      </div>

      {/* Right - A TATA PRODUCT */}
      <div className="text-white font-bold text-sm tracking-widest font-alatsi">
        A TATA PRODUCT
      </div>

    </nav>
  );
}

export default Navbar;