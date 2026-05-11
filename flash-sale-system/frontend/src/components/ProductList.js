import React from 'react';

function ProductList({ products, onAddToCart, loading }) {
  if (loading) {
    return <div className="loading">⏳ Loading products...</div>;
  }

  if (!products || products.length === 0) {
    return <div className="loading">No products available</div>;
  }

  return (
    <div>
      <h2>📦 Available Products</h2>
      <div className="products">
        {products.map(product => (
          <div key={product.id} className="product-card">
            <h3>{product.name}</h3>
            <p>{product.description}</p>
            <div className="price">${product.price?.toFixed(2)}</div>
            <div className="stock">Stock: {product.stock} units</div>
            <button 
              className="add-btn"
              onClick={() => onAddToCart(product)}
            >
              Add to Cart
            </button>
          </div>
        ))}
      </div>
    </div>
  );
}

export default ProductList;
