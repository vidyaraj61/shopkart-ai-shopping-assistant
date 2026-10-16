import { useState, useEffect } from 'react';

const API = 'http://localhost:8080/api';

function ProductCard({ product }) {
  const inStock = product.stock > 0;
  return (
    <div className="card">
      <h3>{product.name}</h3>
      <p className="brand">{product.brand} - {product.category}</p>
      <p className="price">
        {new Intl.NumberFormat('en-IN', {
          style: 'currency', currency: 'INR', maximumFractionDigits: 0,
        }).format(product.price)}
      </p>
      <p className="rating">{product.rating} stars</p>
      <button disabled={!inStock}>
        {inStock ? 'Add to cart' : 'Out of stock'}
      </button>
    </div>
  );
}

export default function ProductGrid() {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetch(`${API}/products`)
      .then((res) => {
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        return res.json();
      })
      .then((data) => setProducts(data))
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <p>Loading products...</p>;
  if (error) return <p>Could not load products: {error}</p>;

  return (
    <div className="grid">
      {products.map((p) => (
        <ProductCard key={p.id} product={p} />
      ))}
    </div>
  );
}