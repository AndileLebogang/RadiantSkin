import { useState, useEffect } from "react";
import reactLogo from "./assets/react.svg";
import viteLogo from "./assets/vite.svg";
import heroImg from "./assets/hero.png";
import "./App.css";

function App() {
  const [title, setTitle] = useState("Default Value");
  useEffect(() => {
    fetch("http://localhost:8080/home")
      .then((response) => response.text())
      .then(text => setTitle(text))
      .catch((error) => {
        console.error("Error fetching title:", error);
      })
      
  }, []);

  return (
    <>
    <h1>React + {title}</h1>
    </>
  )
}

export default App;
