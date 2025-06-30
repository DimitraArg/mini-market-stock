import axios from "axios";

const API = axios.create({
  baseURL: "http://localhost:8080"
});

export const fetchProducts = () => API.get("/products");
export const deleteProduct = (id) => API.delete(`/products/${id}`);