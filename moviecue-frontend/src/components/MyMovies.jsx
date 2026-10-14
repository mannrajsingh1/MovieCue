import { useState, useEffect } from 'react'
import { getMyRatings, deleteRating } from '../api/movieApi'

function MyMovies({ refreshTrigger }) {
    const [ratings, setRatings] = useState([]);
    const [status, setStatus] = useState('')

    useEffect(() => {
        loadRatings();
    }, [refreshTrigger]);

    async function loadRatings() {
        setStatus('Loading...');

        try {
            const data = await getMyRatings();
            setRatings(data);
            setStatus('');
        } catch (e) {
            setStatus(`Error: ${e.message}`);
        }
    }

    async function handleRemove(ratingId) {
        try {
            await deleteRating(ratingId);
            setRatings((prev) => prev.filter((r) => r.id !== ratingId));
        } catch(e) {
            setStatus(`Error: ${e.message}`);
        }
    }

    return (
        <div>
            {status && <p> {status}</p>}
            {ratings.length === 0 && !status && <p> No ratings yet - search for a movie above.</p>}
            <ul>
                {ratings.map((rating) => (
                    <li key={rating.id}>
                        <h3> {rating.movie.title} ({rating.movie.year})</h3>
                        <img src = {rating.movie.posterUrl} alt={rating.movie.title} width = "120" />
                        <strong>{rating.movie.title}</strong> ({rating.movie.year}) - rated {rating.rating}/10{' '}
                        <button onClick={() => handleRemove(rating.id)}>Remove</button>
                    </li>
                ))}
            </ul>
        </div>
    );
}
    export default MyMovies;

