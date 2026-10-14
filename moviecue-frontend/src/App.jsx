import { useState } from 'react';

import SearchMovie from './components/SearchMovie';
import MyMovies from './components/MyMovies';
import Recommendations from './components/Recommendations';

function App() {
  const [refreshTrigger, setRefreshTrigger] = useState(0);

  function handleMovieRated() {
    setRefreshTrigger((prev) => prev + 1);
  }

  return (
    <div style = {{ maxWidth: '700px', margin: '0 auto', padding: '20px', fontFamily: 'sans-serif' }}>
      <h1>
        MovieCue
      </h1>

      <section>
        <h2> Find a Movie </h2>
        <SearchMovie onMovieRated={handleMovieRated} />
      </section>

      <section>
          <h2> My Rated Movies</h2>
          <MyMovies refreshTrigger={refreshTrigger} />
      </section>

      <section>
        <h2> Recommended For You </h2>
        <Recommendations />
      </section>

    </div>
  );
}

export default App;