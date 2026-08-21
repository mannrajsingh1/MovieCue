import { useState, useEffect } from 'react';

function App() {
  const [movies, setMovies] = useState([]);
  const [error, setError] = useState(null)

  useEffect(() => {
    fetch('http://localhost:8080/api/movies')
    .then((res) => res.json())
    .then((data) => setMovies(data))
    .catch((err) => setError(err.message));
  }, []);


  return (
    <div>
      <h1>
        MovieCue - Connection Test
      </h1>
      {error && <p style = {{ color: 'red '}}>Error: {error}</p>}
      <pre> {JSON.stringify(movies,null,2)}</pre>
    </div>
  );
}

export default App;