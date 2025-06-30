import React, { useEffect, useState } from "react";
import axios from "axios";

function CategoryMan({ refresh, triggerRefresh }) {
  const [categories, setCategories] = useState([]);
  const [newCategory, setNewCategory] = useState("");
  const [editingCategory, setEditingCategory] = useState(null);
  const [search, setSearch] = useState("");

  const loadCategories = () => {
    axios.get("http://localhost:8080/categories")
      .then(res => setCategories(res.data))
      .catch(err => console.error("Load error", err));
  };

  useEffect(() => {
    loadCategories();
  }, [refresh]); 

  const handleAddOrUpdate = () => {
    const name = newCategory.trim();
    if (!name) return alert("Please enter a name.");

    if (editingCategory) {
      axios.put(`http://localhost:8080/categories/${editingCategory.id}`, { name })
        .then(() => {
          alert("Category updated.");
          setNewCategory("");
          setEditingCategory(null);
          triggerRefresh(); 
        })
        .catch(err => alert("Error updating."));
    } else {
      axios.post("http://localhost:8080/categories", { name })
        .then(() => {
          alert("Category added.");
          setNewCategory("");
          triggerRefresh(); 
        })
        .catch(err => {
          const msg = err.response?.data;
          if (typeof msg === "string" && msg.startsWith("INACTIVE_EXISTS:")) {
            const id = msg.split(":")[1];
            if (window.confirm("This category was previously deleted. Reactivate it?")) {
              axios.put(`http://localhost:8080/categories/${id}/reactivate`)
                .then(() => {
                  alert("Category reactivated.");
                  setNewCategory("");
                  triggerRefresh(); 
                });
            }
          } else {
            alert("Error adding.");
          }
        });
    }
  };

  const handleDeactivate = (id) => {
    if (window.confirm("Are you sure you want to delete (deactivate) this category?")) {
      axios.put(`http://localhost:8080/categories/${id}/deactivate`)
        .then(() => {
          alert("Category deleted(deactivated).");
          triggerRefresh(); 
        })
        .catch(() => alert("Could not delete. It may be in use."));
    }
  };

  const filtered = categories.filter(c =>
    c.name.toLowerCase().includes(search.toLowerCase())
  );

  return (
    <div style={{ marginTop: "3rem" }}>
      <h2>📁 Category Management</h2>

      <input
        type="text"
        placeholder="Search..."
        value={search}
        onChange={e => setSearch(e.target.value)}
        style={{ marginBottom: 10 }}
      />

      <div>
        <input
          type="text"
          placeholder="Category name"
          value={newCategory}
          onChange={(e) => setNewCategory(e.target.value)}
        />
        <button onClick={handleAddOrUpdate} style={{ marginLeft: 8 }}>
          {editingCategory ? "Update" : "Add"}
        </button>
        {editingCategory && (
          <button onClick={() => {
            setNewCategory("");
            setEditingCategory(null);
          }} style={{ marginLeft: 8 }}>Cancel</button>
        )}
      </div>

      <table border="1" cellPadding="8" style={{ marginTop: 20 }}>
        <thead>
          <tr>
            <th>Name</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {filtered.length === 0 ? (
            <tr><td colSpan="3">No categories found</td></tr>
          ) : (
            filtered.map(c => (
              <tr key={c.id}>
                <td>{c.name}</td>
                <td>
                  <button onClick={() => {
                    setNewCategory(c.name);
                    setEditingCategory(c);
                  }}>✏️ Edit</button>
                  <button onClick={() => handleDeactivate(c.id)} style={{ marginLeft: 8 }}>🗑️ Delete</button>
                </td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
}


export default CategoryMan;
