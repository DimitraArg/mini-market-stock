import React, { useEffect, useState } from "react";
import axios from "axios";

function SupplierMan({ refresh, triggerRefresh }) {
  const [suppliers, setSuppliers] = useState([]);
  const [search, setSearch] = useState("");

  const [formData, setFormData] = useState({
    name: "",
    phone: "",
    email: ""
  });

  const [editing, setEditing] = useState(null);

  const loadSuppliers = () => {
    axios.get("http://localhost:8080/suppliers")
      .then(res => setSuppliers(res.data))
      .catch(err => console.error("Load error", err));
  };

  useEffect(() => {
    loadSuppliers();
  }, [refresh]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSave = () => {
    const { name, phone, email } = formData;
    if (!name.trim()) return alert("Please enter a name.");

    const payload = {
      name: name.trim(),
      phone: phone.trim(),
      email: email.trim()
    };

    if (editing) {
      axios.put(`http://localhost:8080/suppliers/${editing.id}`, payload)
        .then(() => {
          alert("Supplier updated");
          setFormData({ name: "", phone: "", email: "" });
          setEditing(null);
          triggerRefresh();
        })
        .catch(() => alert("Update error"));
    } else {
      axios.post("http://localhost:8080/suppliers", payload)
        .then(() => {
          alert("Supplier added");
          setFormData({ name: "", phone: "", email: "" });
          triggerRefresh();
        })
        .catch(err => {
          const msg = err.response?.data;
          if (typeof msg === "string" && msg.startsWith("INACTIVE_EXISTS:")) {
            const id = msg.split(":")[1];
            if (window.confirm("This supplier was previously deleted. Reactivate it?")) {
              axios.put(`http://localhost:8080/suppliers/${id}/reactivate`)
                .then(() => {
                  alert("Supplier Reactivated");
                  setFormData({ name: "", phone: "", email: "" });
                  triggerRefresh();
                });
            }
          } else {
            alert("Error adding");
          }
        });
    }
  };

  const handleDeactivate = (id) => {
    if (window.confirm("Are you sure you want to delete (deactivate) this supplier?")) {
      axios.put(`http://localhost:8080/suppliers/${id}/deactivate`)
        .then(() => {
          alert("Supplier deactivated");
          triggerRefresh();
        })
        .catch(() => alert("Cannot deactivate. It may be in use."));
    }
  };

  const filtered = suppliers.filter(s =>
    s.name.toLowerCase().includes(search.toLowerCase())
  );

  return (
    <div style={{ marginTop: "2rem" }}>
      <h2>💼 Supplier Management</h2>

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
          name="name"
          placeholder="Supplier name"
          value={formData.name}
          onChange={handleChange}
        />
        <input
          type="text"
          name="phone"
          placeholder="Phone"
          value={formData.phone}
          onChange={handleChange}
          style={{ marginLeft: 8 }}
        />
        <input
          type="email"
          name="email"
          placeholder="Email"
          value={formData.email}
          onChange={handleChange}
          style={{ marginLeft: 8 }}
        />
        <button onClick={handleSave} style={{ marginLeft: 8 }}>
          {editing ? "Update" : "Add"}
        </button>
        {editing && (
          <button
            onClick={() => {
              setFormData({ name: "", phone: "", email: "" });
              setEditing(null);
            }}
            style={{ marginLeft: 8 }}
          >Cancel</button>
        )}
      </div>

      <table border="1" cellPadding="8" style={{ marginTop: 20 }}>
        <thead>
          <tr>
            <th>Name</th>
            <th>Phone</th>
            <th>Email</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {filtered.length === 0 ? (
            <tr><td colSpan="4">No suppliers</td></tr>
          ) : (
            filtered.map(s => (
              <tr key={s.id}>
                <td>{s.name}</td>
                <td>{s.phone || "-"}</td>
                <td>{s.email || "-"}</td>
                <td>
                  <button onClick={() => {
                    setFormData({
                      name: s.name,
                      phone: s.phone || "",
                      email: s.email || ""
                    });
                    setEditing(s);
                  }}>✏️ Edit</button>
                  <button
                    onClick={() => handleDeactivate(s.id)}
                    style={{ marginLeft: 8 }}
                  >🗑️ Delete</button>
                </td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
}

export default SupplierMan;
