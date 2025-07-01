import React, { useEffect, useState } from "react";
import axios from "axios";

function TransactionForm({ transaction, clearEdit, onSave, refresh }) {
  const [products, setProducts] = useState([]);
  const [suppliers, setSuppliers] = useState([]);
  const [formData, setFormData] = useState({
    productId: "",
    supplierId: "",
    type: "IN",
    quantity: 0
  });

  useEffect(() => {
    axios.get("http://localhost:8080/products?active=true")
      .then(res => setProducts(res.data))
      .catch(err => console.error("Product load error", err));

    axios.get("http://localhost:8080/suppliers?active=true")
      .then(res => setSuppliers(res.data))
      .catch(err => console.error("Supplier load error", err));
  }, [refresh]);

  useEffect(() => {
    if (transaction) {
      setFormData({
        productId: transaction.product.id,
        supplierId: transaction.supplier?.id || "",
        type: transaction.type,
        quantity: transaction.quantity
      });
    }
  }, [transaction]);

  const handleChange = e => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = e => {
    e.preventDefault();

    const payload = {
      productId: parseInt(formData.productId),
      supplierId: parseInt(formData.supplierId),
      type: formData.type,
      quantity: parseInt(formData.quantity)
    };

    const method = transaction ? "put" : "post";
    const url = transaction
      ? `http://localhost:8080/transactions/${transaction.id}`
      : "http://localhost:8080/transactions";

    axios[method](url, payload)
      .then(() => {
        alert(transaction ? "Transaction updated." : "Transaction recorded.");
        setFormData({
          productId: "",
          supplierId: "",
          type: "IN",
          quantity: 0
        });
        clearEdit && clearEdit();
        onSave && onSave();
      })
      .catch(err => {
        const msg = err.response?.data;
        if (typeof msg === "string" && msg.startsWith("INACTIVE_PRODUCT:")) {
          alert("Cannot record transaction: Product is inactive.");
        } else {
          alert("Transaction failed.");
        }
      });
  };

  return (
    <form onSubmit={handleSubmit} style={{ marginTop: "2rem" }}>
      <h2>{transaction ? "✏️ Edit Transaction" : "📦 Record Transaction"}</h2>

      <select name="productId" value={formData.productId} onChange={handleChange} required>
        <option value="">-- Select Product --</option>
        {products.map(p => (
          <option key={p.id} value={p.id}>{p.name} ({p.barcode})</option>
        ))}
      </select>

      <select name="supplierId" value={formData.supplierId} onChange={handleChange} required>
        <option value="">-- Select Supplier --</option>
        {suppliers.map(s => (
          <option key={s.id} value={s.id}>{s.name}</option>
        ))}
      </select>

      <select name="type" value={formData.type} onChange={handleChange}>
        <option value="IN">IN (Add Stock)</option>
        <option value="OUT">OUT (Remove Stock)</option>
      </select>

      <input
        type="number"
        name="quantity"
        value={formData.quantity}
        onChange={handleChange}
        placeholder="Quantity"
        required
      />

      <button type="submit">{transaction ? "Update" : "Save"}</button>
      {transaction && (
        <button
          type="button"
          onClick={() => {
            setFormData({ productId: "", supplierId: "", type: "IN", quantity: 0 });
            clearEdit && clearEdit();
          }}
          style={{ marginLeft: 10 }}
        >
          Cancel
        </button>
      )}
    </form>
  );
}

export default TransactionForm;
