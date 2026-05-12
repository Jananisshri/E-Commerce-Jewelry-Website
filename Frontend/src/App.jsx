import { BrowserRouter, Routes, Route } from 'react-router-dom';
import WelcomePage       from './pages/WelcomePage';
import HomePage          from './pages/HomePage';
import CategoryPage      from './pages/CategoryPage';
import ProductDetailPage from './pages/ProductDetailPage';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/"                       element={<WelcomePage />} />
        <Route path="/home"                   element={<HomePage />} />
        <Route path="/category/:categoryName" element={<CategoryPage />} />
        <Route path="/product/:id"            element={<ProductDetailPage />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;