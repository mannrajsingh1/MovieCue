import { useState, useEffect } from 'react'
import { getAllMovies } from '../api/movieApi'

function MyMovies({ refreshTrigger }) {
    const [movies, setMovies] = useState([]);
    const [status, setStatus] = useState('')

    useEffect(() => {
        loadMovies();
    }, [refreshTrigger]);

    async function loadMovies() {
        setStatus('Loading...');

        try {
            const data = await getAllMovies();
            setMovies(data);
            setStatus('');
        } catch (e) {
            setStatus(`Error: ${e.message}`);
        }
    }

    return (
        <div>
            {status && <p> {status}</p>}
            {movies.length === 0 && !status && <p> No movies yet - search for one above.</p>}
            <ul>
                {movies.map((movie) => (
                    <li key={movie.id}>
                        <strong>{movie.title}</strong> ({movie.year})
                        {movie.ratings && movie.ratings.length>0 ? (
                            <span> - rated {movie.ratings[movie.ratings.length - 1].rating}/10</span>
                        ) : (
                            <span> - not rated yet </span>
                        )}
                    </li>
                ))}
            </ul>
        </div>
    );
}
    export default MyMovies;

