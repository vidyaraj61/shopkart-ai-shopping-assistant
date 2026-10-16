import ProductGrid from "./ProductGrid";
import ChatWidget from "./ChatWidget";
import "./App.css";

export default function App() {
  return (
    <div className="app">
      <header className="navbar">
        <div className="brand">
          <span className="brand-icon">S</span>
          <span>ShopKart</span>
        </div>

        <nav>
          <a href="#home">Home</a>
          <a href="#products">Products</a>
          <a href="#assistant">AI Assistant</a>
        </nav>

        <div className="cart-label">🛒 My Cart</div>
      </header>

      <main>
        <section className="hero" id="home">
          <div className="hero-content">
            <span className="eyebrow">YOUR EVERYDAY SHOPPING DESTINATION</span>
            <h1>
              Shopping made
              <br />
              <span>smarter.</span>
            </h1>
            <p>
              Discover your favorites, explore great deals, and get
              personalized shopping assistance.
            </p>
            <a href="#products" className="shop-button">
              Explore Products →
            </a>
          </div>

          <div className="hero-art" aria-hidden="true">
            <span className="art-circle"></span>
            <span className="art-bag">🛍️</span>
            <span className="art-sparkle">✦</span>
            <span className="art-caption">A better way to shop</span>
          </div>
        </section>

        <section className="benefits">
          <div>
            <span>✦</span>
            <div>
              <strong>Curated products</strong>
              <p>Explore our collection</p>
            </div>
          </div>
          <div>
            <span>✓</span>
            <div>
              <strong>Stock availability</strong>
              <p>Know what's available</p>
            </div>
          </div>
          <div>
            <span>✧</span>
            <div>
              <strong>AI shopping help</strong>
              <p>Find what you need</p>
            </div>
          </div>
        </section>

        <div className="content-layout">
          <section className="catalog" id="products">
            <div className="section-heading">
              <div>
                <span className="eyebrow">HANDPICKED FOR YOU</span>
                <h2>Explore our collection</h2>
                <p>Find something you'll love.</p>
              </div>
              <span className="collection-tag">ShopKart Collection</span>
            </div>

            <ProductGrid />
          </section>

          <aside className="assistant" id="assistant">
            <div className="assistant-heading">
              <span className="assistant-icon">✦</span>
              <div>
                <h2>Shopping Assistant</h2>
                <p>Your personal shopping companion</p>
              </div>
            </div>

            <ChatWidget />
          </aside>
        </div>
      </main>

      <footer>
        <div className="brand footer-brand">
          <span className="brand-icon">S</span>
          <span>ShopKart</span>
        </div>
        <p>Your smarter way to shop.</p>
      </footer>
    </div>
  );
}