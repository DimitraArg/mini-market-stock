import React, { useEffect, useState } from "react";
import axios from "axios";
import { fetchProducts } from "../services/ProductService";

function ProductTable({ onEditClick, refresh }) {
  const [products, setProducts] = useState([]);
  const [filtered, setFiltered] = useState([]);

  const [search, setSearch] = useState("");
  const [selectedCategory, setSelectedCategory] = useState("");
  const [categories, setCategories] = useState([]);

  useEffect(() => {
    fetchProducts()
      .then((res) => {
        setProducts(res.data);
        setFiltered(res.data);
      })
      .catch((err) => console.error("Error fetching products:", err));
  }, [refresh]);

  useEffect(() => {
    axios.get("http://localhost:8080/categories")
      .then((res) => setCategories(res.data))
      .catch((err) => console.error("Categories load error", err));
  }, []);

  useEffect(() => {
    filterProducts();
  }, [search, selectedCategory, products]);

  const filterProducts = () => {
    let filtered = [...products];

    if (search.trim() !== "") {
      const term = search.toLowerCase();
      filtered = filtered.filter(p =>
        p.name.toLowerCase().includes(term) ||
        p.barcode.toLowerCase().includes(term)
      );
    }

    if (selectedCategory !== "") {
      filtered = filtered.filter(p =>
        p.category.id === parseInt(selectedCategory)
      );
    }

    setFiltered(filtered);
  };

  const handleDelete = (id) => {
    if (window.confirm("Are you sure you want to delete (deactivate) this product?")) {
      axios.put(`http://localhost:8080/products/${id}/deactivate`)
        .then(() => {
          alert("The product has been deleted (deactivated).");
        })
        .catch((err) => {
          alert("Error while deleting");
          console.error("Delete error:", err);
        });
    }
  };

  return (
    <div>
      <h2>🧾 Product List</h2>

      <div style={{ marginBottom: 12 }}>
        <input
          type="text"
          placeholder="Search..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />
        <select
          value={selectedCategory}
          onChange={(e) => setSelectedCategory(e.target.value)}
          style={{ marginLeft: 8 }}
        >
          <option value="">-- Filter by Category --</option>
          {categories.map((c) => (
            <option key={c.id} value={c.id}>{c.name}</option>
          ))}
        </select>
      </div>

      <table border="1" cellPadding="8">
        <thead>
          <tr>
            <th>Name</th>
            <th>Barcode</th>
            <th>Category</th>
            <th>Quantity</th>
            <th>Price</th>
            <th>Vat</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {filtered.length === 0 ? (
            <tr><td colSpan="8">No products found</td></tr>
          ) : (
            filtered.map((p) => (
              <tr key={p.id}>
                <td>{p.name}</td>
                <td>{p.barcode}</td>
                <td>{p.category.name}</td>
                <td>{p.quantity}</td>
                <td>{p.price} €</td>
                <td>{p.vat} %</td>
                <td>
                  <button onClick={() => onEditClick(p)}>✏️ Edit</button>
                  <button onClick={() => handleDelete(p.id)}>🗑️ Delete</button>
                </td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
}

export default ProductTable;
