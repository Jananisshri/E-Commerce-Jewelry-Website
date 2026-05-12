import { useEffect, useRef } from 'react';

const CATEGORIES = [
  'Rings',
  'Earrings',
  'Necklace',
  'Bangles',
  'Nose pins',
  'Bracelets',
];

function SearchDropdown({ onSelect, onClose }) {
  const ref = useRef(null);

  useEffect(() => {
    const handleOutside = (e) => {
      if (ref.current && !ref.current.contains(e.target)) {
        onClose();
      }
    };
    document.addEventListener('mousedown', handleOutside);
    return () => document.removeEventListener('mousedown', handleOutside);
  }, [onClose]);

  return (
    <div
      ref={ref}
      className="absolute top-12 left-0 w-full bg-[#c8d5b9] rounded-2xl shadow-xl z-50 overflow-hidden"
    >
      {CATEGORIES.map((category) => (
        <div
          key={category}
          onClick={() => onSelect(category)}
          className="px-6 py-3 text-gray-700 hover:bg-[#8a9e7e] hover:text-white cursor-pointer text-sm transition-colors duration-150 italic"
        >
          {category}
        </div>
      ))}
    </div>
  );
}

export default SearchDropdown;