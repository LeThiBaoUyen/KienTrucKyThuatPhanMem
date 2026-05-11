import React, { useState, useEffect } from 'react';
import './App.css';
import ProductList from './components/ProductList';
import Cart from './components/Cart';
import API_CONFIG from './api-config';
import createApiClient from './api-client';

function App() {
  const [userId] = useState('user-' + Math.random().toString(36).substr(2, 9));
  const [products, setProducts] = useState([]);
  const [cart, setCart] = useState([]);
  const [cartTotal, setCartTotal] = useState(0);
  const [loading, setLoading] = useState(false);
  const [activeTab, setActiveTab] = useState('products');
  const [orders, setOrders] = useState([]);
  const [message, setMessage] = useState('');

  const productClient = createApiClient(API_CONFIG.PRODUCT_API);
  const cartClient = createApiClient(API_CONFIG.CART_API);
  const orderClient = createApiClient(API_CONFIG.ORDER_API);
  const stockClient = createApiClient(API_CONFIG.STOCK_API);

  // Khởi tạo dữ liệu khi load app
  useEffect(() => {
    initializeSystem();
  }, []);

  const initializeSystem = async () => {
    try {
      setMessage('📚 Initializing system...');
      
      // Init products
      await productClient.post('/init');
      
      // Init stock
      await stockClient.post('/init');
      
      // Load products
      await loadProducts();
      
      setMessage('✅ System initialized!');
      setTimeout(() => setMessage(''), 3000);
    } catch (error) {
      console.error('Init error:', error);
      setMessage('❌ Failed to initialize');
    }
  };

  const loadProducts = async () => {
    try {
      setLoading(true);
      const response = await productClient.get('/');
      setProducts(response.data || []);
    } catch (error) {
      console.error('Error loading products:', error);
      setMessage('❌ Failed to load products');
    } finally {
      setLoading(false);
    }
  };

  const loadCart = async () => {
    try {
      const response = await cartClient.get(`/${userId}`);
      setCart(response.data?.items || []);
      setCartTotal(response.data?.totalPrice || 0);
    } catch (error) {
      console.error('Error loading cart:', error);
    }
  };

  const addToCart = async (product) => {
    try {
      setMessage('⏳ Adding to cart...');
      const start = performance.now();
      
      const response = await cartClient.post('/add', {
        userId,
        productId: product.id,
        productName: product.name,
        price: product.price,
        quantity: 1
      });

      const duration = (performance.now() - start).toFixed(2);
      setCart(response.data?.items || []);
      setCartTotal(response.data?.totalPrice || 0);
      
      setMessage(`✅ Added to cart (${duration}ms) ⚡`);
      setTimeout(() => setMessage(''), 3000);
    } catch (error) {
      console.error('Error adding to cart:', error);
      setMessage('❌ Failed to add to cart');
    }
  };

  const removeFromCart = async (productId) => {
    try {
      setMessage('⏳ Removing from cart...');
      const response = await cartClient.delete(`/${userId}/item/${productId}`);
      setCart(response.data?.items || []);
      setCartTotal(response.data?.totalPrice || 0);
      setMessage('✅ Removed from cart');
      setTimeout(() => setMessage(''), 3000);
    } catch (error) {
      console.error('Error removing from cart:', error);
      setMessage('❌ Failed to remove from cart');
    }
  };

  const checkout = async () => {
    try {
      if (cart.length === 0) {
        setMessage('⚠️ Cart is empty!');
        return;
      }

      setMessage('💳 Processing checkout...');
      const start = performance.now();

      const response = await orderClient.post('/checkout', {
        userId,
        shippingAddress: '123 Main St',
        phoneNumber: '0123456789'
      });

      const duration = (performance.now() - start).toFixed(2);

      setCart([]);
      setCartTotal(0);
      
      setMessage(`✅ Order placed successfully! (${duration}ms) 🚀`);
      await loadOrders();
      setActiveTab('orders');
      setTimeout(() => setMessage(''), 5000);
    } catch (error) {
      console.error('Checkout error:', error);
      setMessage('❌ Checkout failed: ' + (error.response?.data?.message || error.message));
    }
  };

  const loadOrders = async () => {
    try {
      const response = await orderClient.get(`/user/${userId}`);
      setOrders(response.data || []);
    } catch (error) {
      console.error('Error loading orders:', error);
    }
  };

  return (
    <div className="App">
      <header className="App-header">
        <h1>🛍️ Flash Sale System</h1>
        <p>Space-Based Architecture | Redis Data Grid | Low Latency</p>
        <p className="user-id">User: {userId}</p>
      </header>

      {message && <div className="message">{message}</div>}

      <nav className="nav-tabs">
        <button 
          className={activeTab === 'products' ? 'active' : ''} 
          onClick={() => setActiveTab('products')}
        >
          📦 Products ({products.length})
        </button>
        <button 
          className={activeTab === 'cart' ? 'active' : ''} 
          onClick={() => {
            setActiveTab('cart');
            loadCart();
          }}
        >
          🛒 Cart ({cart.length})
        </button>
        <button 
          className={activeTab === 'orders' ? 'active' : ''} 
          onClick={() => {
            setActiveTab('orders');
            loadOrders();
          }}
        >
          📋 Orders ({orders.length})
        </button>
      </nav>

      <div className="content">
        {activeTab === 'products' && (
          <ProductList 
            products={products} 
            onAddToCart={addToCart}
            loading={loading}
          />
        )}

        {activeTab === 'cart' && (
          <Cart 
            items={cart} 
            total={cartTotal}
            onRemove={removeFromCart}
            onCheckout={checkout}
          />
        )}

        {activeTab === 'orders' && (
          <div className="orders">
            <h2>📋 Your Orders</h2>
            {orders.length === 0 ? (
              <p>No orders yet</p>
            ) : (
              <div className="orders-list">
                {orders.map(order => (
                  <div key={order.id} className="order-card">
                    <h3>Order #{order.id}</h3>
                    <p>Status: <strong>{order.status}</strong></p>
                    <p>Total: <strong>${order.totalPrice?.toFixed(2)}</strong></p>
                    <p>Items: {order.items?.length}</p>
                    <div className="order-items">
                      {order.items?.map((item, idx) => (
                        <div key={idx} className="order-item">
                          {item.productName} x{item.quantity} @ ${item.price}
                        </div>
                      ))}
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        )}
      </div>

      <footer className="App-footer">
        <p>⚡ Low Latency | 🔥 High Throughput | 💾 Redis Data Grid</p>
      </footer>
    </div>
  );
}

export default App;
