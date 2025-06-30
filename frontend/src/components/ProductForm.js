import React, { useEffect, useState } from "react";
import axios from "axios";

function ProductForm({ product, clearEdit, onSave }) {
  const [formData, setFormData] = useState({
    name: "",
    barcode: "",
    price: 0,
    vat: 0,
    categoryId: ""
  });

  const [categories, setCategories] = useState([]);

  useEffect(() => {
    if (product) {
      setFormData({
        name: product.name,
        barcode: product.barcode,
        price: product.price,
        vat: product.vat,
        categoryId: product.category.id
      });
    }
  }, [product]);

  useEffect(() => {
    axios.get("http://localhost:8080/categories").then(res => setCategories(res.data));
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const payload = {
      ...formData,
      price: parseFloat(formData.price),
      vat: parseFloat(formData.vat),
      categoryId: parseInt(formData.categoryId)
    };

    const method = product ? "put" : "post";
    const url = product ? `http://localhost:8080/products/${product.id}` : "http://localhost:8080/products";

    axios[method](url, payload)
      .then(() => {
        alert(product ? "Product updated!" : "Product created!");
        setFormData({ name: "", barcode: "", price: 0, vat: 0, categoryId: "" });
        clearEdit && clearEdit();
        onSave && onSave();
      })
      .catch(err => {
        alert("Save error");
        console.error("Error:", err);
      });
  };

  return (
    <form onSubmit={handleSubmit}>
      <h2>{product ? "✏️ Edit Product" : "➕ Add Product"}</h2>
      <label>Name</label>
      <input name="name" value={formData.name} onChange={handleChange} required />

      <label>Barcode</label>
      <input name="barcode" value={formData.barcode} onChange={handleChange} required />

      <label>Price</label>
      <input name="price" type="number" value={formData.price} onChange={handleChange} required />

      <label>VAT</label>
      <input name="vat" type="number" value={formData.vat} onChange={handleChange} required />

      <label>Category</label>
      <select name="categoryId" value={formData.categoryId} onChange={handleChange} required>
        <option value="">-- Select Category --</option>
        {categories.map(c => <option key={c.id} value={c.id}>{c.name}</option>)}
      </select>

      <button type="submit">{product ? "Update" : "Save"}</button>
    </form>
  );
}

export default ProductForm;