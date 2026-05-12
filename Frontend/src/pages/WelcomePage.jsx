import { useNavigate } from 'react-router-dom';
import logoImg from '../assets/images/logo.png';
import heroImg from '../assets/images/welcome.jpg';

function WelcomePage() {
  const navigate = useNavigate();

  return (
    <div className="flex h-screen w-full relative overflow-hidden">

      {/* Left Half */}
      <div className="w-1/2 flex flex-col justify-between p-6 z-10">

        {/* Top Left - A TATA PRODUCT */}
        <div className="text-white font-bold text-sm tracking-widest font-alatsi">
          A TATA PRODUCT
        </div>

        {/* Center - Logo Card */}
        <div className="flex flex-col items-center gap-8">
          <div className="bg-[#c8d5b9] rounded-3xl px-16 py-16 flex items-center gap-6 w-full max-w-lg">

            {/* Logo Image */}
            <img
              src={logoImg}
              alt="Tanishq Logo"
              className="h-20 w-auto"
            />

            <div>
              <h1 className="text-tanishq-gold font-bold text-4xl tracking-widest font-cinzel">
                TANISHQ
              </h1>
              <p className="text-tanishq-gold text-xs tracking-widest mt-2 font-montserrat">
                CRAFTING EVERY MOMENT IN GOLD
              </p>
            </div>
          </div>

          {/* Explore Button */}
          <button
            onClick={() => navigate('/home')}
            className="bg-tanishq-gold text-white font-bold text-sm tracking-widest px-16 py-4 rounded-full hover:opacity-90 transition-opacity font-montserrat"
          >
            EXPLORE
          </button>
        </div>

        {/* Bottom - Contact Info */}
        <div className="flex justify-between items-center text-white text-xs font-montserrat">
          <div className="flex items-center gap-1">
            <svg className="w-4 h-4" fill="currentColor" viewBox="0 0 24 24">
              <path d="M12 2C8.13 2 5 5.13 5 9c0 5.25 7 13 7 13s7-7.75 7-13c0-3.87-3.13-7-7-7zm0 9.5c-1.38 0-2.5-1.12-2.5-2.5s1.12-2.5 2.5-2.5 2.5 1.12 2.5 2.5-1.12 2.5-2.5 2.5z"/>
            </svg>
            <span>Hosur, Tamil Nadu</span>
          </div>
          <span>Email: tanishqhosur@gmail.com</span>
          <span>Phone: +919876543210</span>
        </div>

      </div>

      {/* Right Half - Jewellery Woman Image */}
      <div className="w-1/2 h-full">
        <img
          src={heroImg}
          alt="Tanishq Jewellery"
          className="w-full h-full object-cover"
        />
      </div>

    </div>
  );
}

export default WelcomePage;