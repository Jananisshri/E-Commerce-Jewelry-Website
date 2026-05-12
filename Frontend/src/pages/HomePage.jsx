import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Navbar from '../components/Navbar';
import productService from '../services/productService';

import heroLeft   from '../assets/images/left.jpg';
import heroCenter from '../assets/images/centre.jpg';
import heroRight  from '../assets/images/right.jpg';

function HomePage() {
  const [featuredProducts, setFeaturedProducts] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    productService.getFeaturedProducts()
      .then(setFeaturedProducts)
      .catch(console.error);
  }, []);

  return (
    <div className="min-h-screen">

      {/* Navbar */}
      <Navbar />

      {/* ── SCREEN 1 ── Hero Section - exactly like Image 2 */}

      {/* Sage green background strip behind images */}
      <div className="mx-6 mt-6 rounded-3xl bg-[#8a9e7e] p-0 overflow-hidden">
        <div className="flex h-[420px]">

          {/* Left Image */}
          <div className="w-1/3 overflow-hidden rounded-tl-3xl rounded-bl-3xl">
            <img
              src={heroLeft}
              alt="Jewellery"
              className="w-full h-full object-cover"
            />
          </div>

          {/* Center Image */}
          <div className="w-1/3 overflow-hidden">
            <img
              src={heroCenter}
              alt="Jewellery"
              className="w-full h-full object-cover object-bottom"
            />
          </div>

          {/* Right Image */}
          <div className="w-1/3 overflow-hidden rounded-tr-3xl rounded-br-3xl">
            <img
              src={heroRight}
              alt="Jewellery"
              className="w-full h-full object-cover"
            />
          </div>

        </div>
      </div>

      {/* ── SCREEN 2 (scroll down) ── Special Collection */}
      <div className="px-8 py-10 min-h-screen">

        {/* Heading */}
        <h2 className="text-tanishq-gold font-bold text-2xl italic mb-8">
          Special Collection
        </h2>

        {/* 4 Featured Product Cards */}
        <div className="grid grid-cols-4 gap-8">
          {featuredProducts.map((product) => (
            <div
              key={product.id}
              className="flex flex-col items-center gap-4 cursor-pointer"
              onClick={() => navigate(`/product/${product.id}`)}
            >
              {/* Product Image */}
              <div className="w-full rounded-2xl overflow-hidden h-56">
                <img
                  src={`http://localhost:8080${product.imageUrl}`}
                  alt={product.name}
                  className="w-full h-full object-cover"
                />
              </div>

              {/* Gold pill label */}
              <button className="bg-tanishq-gold text-white text-sm font-bold px-6 py-2 rounded-full">
                {product.name}
              </button>

            </div>
          ))}
        </div>

      </div>

    </div>
  );
}

export default HomePage;