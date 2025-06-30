import React, { useState } from "react";
import ProductForm from "./components/ProductForm";
import ProductTable from "./components/ProductTable";
import CategoryMan from "./components/CategoryMan";
import SupplierMan from "./components/SupplierMan";
import TransactionForm from "./components/TransactionForm";
import TransactionTable from "./components/TransactionTable";

function App() {
  const [editingProduct, setEditingProduct] = useState(null);
  const [refresh, setRefresh] = useState(false);
  const triggerRefresh = () => setRefresh((r) => !r);
  const [editingTransaction, setEditingTransaction] = useState(null);

  return (
     <div style={{ padding: "2rem" }}>
      <h1>
        <img
          src="/favicon1.png"
          alt="Logo"
          style={{ width: "32px", height: "32px", verticalAlign: "middle", marginRight: "10px" }}
        />
        MiniMarket Stock
      </h1>

      <ProductForm
        product={editingProduct}
        clearEdit={() => setEditingProduct(null)}
        onSave={triggerRefresh}
      />

      <ProductTable
        onEditClick={(product) => setEditingProduct(product)}
        refresh={refresh}
      />

      <div style={{ display: "flex", gap: "2rem", marginTop: "2rem" }}>
        <CategoryMan refresh={refresh} triggerRefresh={triggerRefresh}/>
        <SupplierMan refresh={refresh} triggerRefresh={triggerRefresh}/>
      </div>

      <TransactionForm transaction={editingTransaction} clearEdit={() => setEditingTransaction(null)} onSave={triggerRefresh} />
      <TransactionTable refresh={refresh} onEditClick={setEditingTransaction} />  
    </div>
  );
}

export default App;
