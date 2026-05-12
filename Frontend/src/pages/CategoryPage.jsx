import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import Navbar from '../components/Navbar';
import productService from '../services/productService';

function CategoryPage() {
  const { categoryName } = useParams();
  const [products, setProducts]   = useState([]);
  const [loading, setLoading]     = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    setLoading(true);
    productService.getProductsByCategory(categoryName)
      .then((data) => {
        setProducts(data);
        setLoading(false);
      })
      .catch((err) => {
        console.error(err);
        setLoading(false);
      });
  }, [categoryName]);

  return (
    <div className="min-h-screen">

      {/* Navbar */}
      <Navbar />

      {/* Category Heading */}
      <div className="px-8 py-6">
        <h2 className="text-tanishq-gold font-bold text-2xl italic">
          {categoryName}
        </h2>
      </div>

      {/* Loading */}
      {loading && (
        <div className="flex justify-center items-center h-64">
          <p className="text-white text-lg">Loading...</p>
        </div>
      )}

      {/* 8 Product Cards - 4 per row */}
      {!loading && (
        <div className="px-8 pb-8">
          <div className="grid grid-cols-4 gap-6">
            {products.map((product) => (
              <div
                key={product.id}
                className="flex flex-col items-center gap-3 cursor-pointer"
                onClick={() => navigate(`/product/${product.id}`)}
              >
                {/* Product Image */}
                <div className="w-full rounded-2xl overflow-hidden aspect-square bg-[#c8d5b9]">
                  <img
                    src={`http://localhost:8080${product.imageUrl}`}
                    alt={product.name}
                    className="w-full h-full object-cover"
                  />
                </div>

                {/* Product Name Label */}
                <button className="bg-tanishq-gold text-white text-sm font-bold px-4 py-2 rounded-full w-full text-center">
                  {product.name}
                </button>

              </div>
            ))}
          </div>
        </div>
      )}

    </div>
  );
}

export default CategoryPage;