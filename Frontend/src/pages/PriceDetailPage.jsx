import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import Navbar from '../components/Navbar';
import productService from '../services/productService';

function ProductDetailPage() {
  const { id } = useParams();
  const [product, setProduct]         = useState(null);
  const [priceDetails, setPriceDetails] = useState(null);
  const [loading, setLoading]         = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    Promise.all([
      productService.getProductById(id),
      productService.getPriceDetails(id),
    ])
      .then(([prod, price]) => {
        setProduct(prod);
        setPriceDetails(price);
        setLoading(false);
      })
      .catch((err) => {
        console.error(err);
        setLoading(false);
      });
  }, [id]);

  const fmt = (val) =>
    Number(val).toLocaleString('en-IN', { minimumFractionDigits: 2, maximumFractionDigits: 2 });

  if (loading) {
    return (
      <div className="min-h-screen">
        <Navbar />
        <div className="flex justify-center items-center h-64">
          <p className="text-white text-lg font-montserrat">Loading...</p>
        </div>
      </div>
    );
  }

  if (!product) {
    return (
      <div className="min-h-screen">
        <Navbar />
        <div className="flex justify-center items-center h-64">
          <p className="text-white text-lg font-montserrat">Product not found.</p>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen">

      {/* Navbar */}
      <Navbar />

      {/* Category Heading */}
      <div className="px-8 py-6">
        <h2 className="text-tanishq-gold font-bold text-2xl italic font-montserrat">
          {product.category}
        </h2>
      </div>

      {/* ── SECTION 1 — Product Detail Card (Image 6) ── */}
      <div className="px-8 pb-8">
        <div className="bg-[#8a9e7e] rounded-3xl p-8 flex gap-8 items-center">

          {/* Left - Product Image */}
          <div className="w-2/5 rounded-2xl overflow-hidden aspect-square flex-shrink-0">
            <img
              src={`http://localhost:8080${product.imageUrl}`}
              alt={product.name}
              className="w-full h-full object-cover"
            />
          </div>

          {/* Right - Product Info Box */}
          <div className="flex-1 bg-tanishq-gold rounded-3xl p-8 flex flex-col gap-4">

            {/* Product Name */}
            <h1 className="text-white font-bold text-2xl uppercase tracking-wide font-montserrat">
              {product.name}
            </h1>

            {/* Details */}
            <div className="flex flex-col gap-3 text-white text-lg font-montserrat">
              <p><span className="font-semibold">Weight: </span>{product.weight}g</p>
              <p><span className="font-semibold">Karatage: </span>{product.karatage}</p>
              <p><span className="font-semibold">Materials: </span>{product.materials}</p>
            </div>

            {/* Total Price */}
            {priceDetails && (
              <p className="text-white font-bold text-2xl mt-2 font-montserrat">
                ₹{fmt(priceDetails.totalPrice)}
              </p>
            )}

          </div>
        </div>

        {/* Buy Now Button */}
        <div className="flex justify-center mt-8 mb-8">
          <button
            onClick={() => {
              document.getElementById('price-breakdown').scrollIntoView({ behavior: 'smooth' });
            }}
            className="bg-tanishq-green text-tanishq-gold font-bold text-lg px-16 py-4 rounded-full hover:opacity-90 transition-opacity border border-tanishq-gold font-montserrat"
          >
            Buy Now!
          </button>
        </div>

        {/* ── SECTION 2 — Price Breakdown (Image 7) ── */}
        {priceDetails && (
          <div id="price-breakdown" className="bg-[#8a9e7e] rounded-3xl p-10 mt-4">

            {/* Gold Price Heading */}
            <h3 className="text-black text-2xl font-light mb-8 tracking-wide font-montserrat">
              Gold Price ({priceDetails.karatage}): ₹{fmt(priceDetails.goldRatePerGram)}
            </h3>

            {/* Price Breakdown Table */}
            <table className="w-3/4 border-collapse border border-black">
              <thead>
                <tr>
                  <th className="border border-black p-4 w-1/3 bg-[#8a9e7e]"></th>
                  <th className="border border-black p-4 w-1/3 text-center font-normal text-black font-montserrat">
                    Percentage (%) / Grams
                  </th>
                  <th className="border border-black p-4 w-1/3 text-center font-normal text-black font-montserrat">
                    Cost (₹)
                  </th>
                </tr>
              </thead>

              <tbody>
                {/* Weight */}
                <tr>
                  <td className="border border-black p-4 text-center font-semibold text-black font-montserrat">Weight</td>
                  <td className="border border-black p-4 text-right text-black font-montserrat">{priceDetails.weight}g</td>
                  <td className="border border-black p-4 text-right text-black font-montserrat">₹{fmt(priceDetails.goldValue)}</td>
                </tr>

                {/* Making */}
                <tr>
                  <td className="border border-black p-4 text-center font-semibold text-black font-montserrat">Making</td>
                  <td className="border border-black p-4 text-right text-black font-montserrat">{priceDetails.makingPercent}%</td>
                  <td className="border border-black p-4 text-right text-black font-montserrat">₹{fmt(priceDetails.makingValue)}</td>
                </tr>

                {/* Wastage */}
                <tr>
                  <td className="border border-black p-4 text-center font-semibold text-black font-montserrat">Wastage</td>
                  <td className="border border-black p-4 text-right text-black font-montserrat">{priceDetails.wastagePercent}%</td>
                  <td className="border border-black p-4 text-right text-black font-montserrat">₹{fmt(priceDetails.wastageValue)}</td>
                </tr>

                {/* Tax */}
                <tr>
                  <td className="border border-black p-4 text-center font-semibold text-black font-montserrat">Tax</td>
                  <td className="border border-black p-4 text-right text-black font-montserrat">{priceDetails.gstPercent}%</td>
                  <td className="border border-black p-4 text-right text-black font-montserrat">₹{fmt(priceDetails.gstAmount)}</td>
                </tr>

                {/* Total */}
                <tr>
                  <td className="border border-black p-4 text-center font-semibold text-black font-montserrat">Total</td>
                  <td colSpan={2} className="border border-black p-4 text-right font-bold text-black font-montserrat">
                    ₹{fmt(priceDetails.totalPrice)}
                  </td>
                </tr>
              </tbody>
            </table>

            {/* BUY NOW → Download PDF */}
            <div className="flex justify-center mt-10">
              <button
                onClick={() => productService.downloadPdf(id)}
                className="bg-tanishq-green text-tanishq-gold font-bold text-lg px-16 py-4 rounded-full hover:opacity-90 transition-opacity border border-tanishq-gold font-montserrat"
              >
                BUY NOW
              </button>
            </div>

          </div>
        )}

      </div>

    </div>
  );
}

export default ProductDetailPage;