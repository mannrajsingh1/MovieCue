import { useState } from 'react';
import { searchMovie, rateMovie } from '../api/movieApi';


function SearchMovie({ onMovieRated }) {
    const [title, setTitle] = useState('');
    const [result, setResult] = useState(null);
    const [score, setScore] = useState('');
    const [status, setStatus] = useState('');

    async function handleSearch() {
        if (!title.trim()) {
            return;
        }

        setStatus('Searching...');
        setResult(null);

        try {
            const movie = await searchMovie(title);
            setResult(movie);
            setStatus('');
        }
        catch (e) {
            setStatus(`Error: ${e.message}`);
        }

    }

    async function handleRate() {
        if (!result || !score) {
            return;
    }

    setStatus('Saving rating....');

    try {
        await rateMovie(result.id, parseFloat(score));
        setStatus('Rated!');
        setScore('');
        onMovieRated(); //refreshing the movie list
    } catch (e) {
        setStatus(`Error: ${e.message}`);
    }
} 

    return (
        <div>
            <input
            type = "text"
            value = {title}
            onChange = {(e) => setTitle(e.target.value)}
            placeholder = "e.g Inception"
            />
            <button onClick = {handleSearch}> Search </button>

            {status && <p>{status}</p>}

            {result &&  (
                <div>
                    <h3> {result.title} ({result.year})</h3>
                    <img src = {result.posterUrl} alt={result.title} width = "120" />
                    <p> {result.genre}</p>
                    <p>Director: {result.director}</p>

                    <input
                    type="number"
                    min="0"
                    max="10"
                    step="0.5"
                    value={score}
                    onChange={(e) => setScore(e.target.value)}
                    placeholder="Your rating (0-10)" 
                    />
                    <button onClick={handleRate}>Rate</button>
                    </div>
            )}
        </div>
    );
}

export default SearchMovie;