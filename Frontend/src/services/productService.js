import api from './api';

const productService = {

  getAllProducts: async () => {
    const res = await api.get('/api/products');
    return res.data.data;
  },

  getProductById: async (id) => {
    const res = await api.get(`/api/products/${id}`);
    return res.data.data;
  },

  getProductsByCategory: async (category) => {
    const res = await api.get(`/api/products/category/${category}`);
    return res.data.data;
  },

  getFeaturedProducts: async () => {
    const res = await api.get('/api/products/featured');
    return res.data.data;
  },

  searchProducts: async (keyword) => {
    const res = await api.get(`/api/products/search?keyword=${keyword}`);
    return res.data.data;
  },

  getPriceDetails: async (id) => {
    const res = await api.get(`/api/products/${id}/price-details`);
    return res.data.data;
  },

  downloadPdf: (id) => {
    window.open(`http://localhost:8080/api/products/${id}/download-pdf`, '_blank');
  },
};

export default productService;