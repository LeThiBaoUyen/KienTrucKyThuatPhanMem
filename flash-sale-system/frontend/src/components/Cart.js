import React from 'react';

function Cart({ items, total, onRemove, onCheckout }) {
  return (
    <div className="cart">
      <h2>🛒 Shopping Cart</h2>
      
      {items && items.length === 0 ? (
        <p>Your cart is empty</p>
      ) : (
        <>
          <div className="cart-items">
            {items.map((item, idx) => (
              <div key={idx} className="cart-item">
                <div className="cart-item-info">
                  <div className="cart-item-name">{item.productName}</div>
                  <div className="cart-item-price">
                    ${item.price?.toFixed(2)} x {item.quantity} = ${(item.price * item.quantity)?.toFixed(2)}
                  </div>
                </div>
                <button 
                  className="remove-btn"
                  onClick={() => onRemove(item.productId)}
                >
                  Remove
                </button>
              </div>
            ))}
          </div>

          <div className="cart-summary">
            <div className="cart-total">
              Total: ${total?.toFixed(2)}
            </div>
            <button 
              className="checkout-btn"
              onClick={onCheckout}
            >
              💳 Checkout
            </button>
          </div>
        </>
      )}
    </div>
  );
}

export default Cart;
