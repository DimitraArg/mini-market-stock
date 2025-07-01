import React, { useEffect, useState } from "react";
import axios from "axios";

function TransactionTable({ refresh, onEditClick, onSave, triggerRefresh }) {
  const [transactions, setTransactions] = useState([]);
  const [filtered, setFiltered] = useState([]);
  const [products, setProducts] = useState([]);
  const [suppliers, setSuppliers] = useState([]);

  const [type, setType] = useState("");
  const [from, setFrom] = useState("");
  const [to, setTo] = useState("");
  const [productId, setProductId] = useState("");
  const [supplierId, setSupplierId] = useState("");

  useEffect(() => {
    axios.get("http://localhost:8080/transactions")
      .then(res => {
        const safeData = Array.isArray(res.data) ? res.data.filter(t => t && t.id) : [];
        setTransactions(safeData);
        setFiltered(safeData);
      });

    axios.get("http://localhost:8080/products")
      .then(res => {
        const safeProducts = Array.isArray(res.data) ? res.data.filter(p => p && p.id) : [];
        setProducts(safeProducts);
      });

    axios.get("http://localhost:8080/suppliers")
      .then(res => {
        const safeSuppliers = Array.isArray(res.data) ? res.data.filter(s => s && s.id) : [];
        setSuppliers(safeSuppliers);
      });
  }, [refresh]);

  useEffect(() => {
    let filteredList = [...transactions];

    if (type) {
      filteredList = filteredList.filter(t => t.type === type);
    }

    if (from) {
      const fromDate = new Date(from);
      filteredList = filteredList.filter(t => new Date(t.timestamp) >= fromDate);
    }

    if (to) {
      const toDate = new Date(to);
      filteredList = filteredList.filter(t => new Date(t.timestamp) <= toDate);
    }

    if (productId) {
      filteredList = filteredList.filter(t => t.product?.id === parseInt(productId));
    }

    if (supplierId) {
      filteredList = filteredList.filter(t => t.supplier?.id === parseInt(supplierId));
    }

    setFiltered(filteredList);
  }, [type, from, to, productId, supplierId, transactions]);

  const handleDeactivate = (id) => {
    if (window.confirm("Are you sure you want to delete (deactivate) this transaction?")) {
      axios.put(`http://localhost:8080/transactions/${id}/deactivate`)
        .then(() => {
          alert("Transaction deactivated.");
          onSave && onSave();
          triggerRefresh();
        })
        .catch(err => {
          alert("Error deactivating transaction");
          console.error(err);
        });
    }
  };

  const totalIncome = filtered
    .filter(t => t.type === "OUT")
    .reduce((sum, t) => {
      const price = t.product?.price ?? 0;
      const vat = t.product?.vat ?? 0;
      return sum + t.quantity * price * (1 + vat / 100);
    }, 0);

  const totalExpense = filtered
    .filter(t => t.type === "IN")
    .reduce((sum, t) => {
      const price = t.product?.price ?? 0;
      const vat = t.product?.vat ?? 0;
      return sum + t.quantity * price * (1 + vat / 100);
    }, 0);

  return (
    <div style={{ marginTop: "3rem" }}>
      <h2>📊 Transactions</h2>

      <div style={{ display: "flex", gap: 10, flexWrap: "wrap", marginBottom: 10 }}>
        <select value={type} onChange={e => setType(e.target.value)}>
          <option value="">All types</option>
          <option value="IN">IN</option>
          <option value="OUT">OUT</option>
        </select>

        <input type="date" value={from} onChange={e => setFrom(e.target.value)} />
        <input type="date" value={to} onChange={e => setTo(e.target.value)} />

        <select value={productId} onChange={e => setProductId(e.target.value)}>
          <option value="">All products</option>
          {products.map(p => (
            <option key={p.id} value={p.id}>
              {p.name} ({p.barcode})
            </option>
          ))}
        </select>

        <select value={supplierId} onChange={e => setSupplierId(e.target.value)}>
          <option value="">All suppliers</option>
          {suppliers.map(s => (
            <option key={s.id} value={s.id}>{s.name}</option>
          ))}
        </select>
      </div>

      <table border="1" cellPadding="8" style={{ width: "100%" }}>
        <thead>
          <tr>
            <th>Type</th>
            <th>Product</th>
            <th>Barcode</th>
            <th>Supplier</th>
            <th>Quantity</th>
            <th>Timestamp</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {filtered.length === 0 ? (
            <tr><td colSpan="7">No transactions found</td></tr>
          ) : (
            filtered.map(t => (
              <tr key={t.id}>
                <td>{t.type}</td>
                <td>{t.product?.name || "-"}</td>
                <td>{t.product?.barcode || "-"}</td>
                <td>{t.supplier?.name || "-"}</td>
                <td>{t.quantity}</td>
                <td>{new Date(t.timestamp).toLocaleString()}</td>
                <td>
                  <button onClick={() => onEditClick(t)}>✏️ Edit</button>
                  <button onClick={() => handleDeactivate(t.id)} style={{ marginLeft: 6 }}>🗑️ Delete</button>
                </td>
              </tr>
            ))
          )}
        </tbody>
      </table>

      <div style={{ marginTop: "1rem", fontWeight: "bold" }}>
        <span style={{ color: "green", marginRight: 20 }}>
          Total Income : {totalIncome.toFixed(2)} €
        </span>
        <span style={{ color: "red" }}>
          Total Expense : {totalExpense.toFixed(2)} €
        </span>
      </div>
    </div>
  );
}

export default TransactionTable;
