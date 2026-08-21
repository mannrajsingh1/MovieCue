import { useState } from 'react';
import { getRecommendations } from '../api/movieApi';

function Recommendations(){
    const [recommendations, setRecommendations] = useState([]);
    const [status, setStatus] = useState('');

    async function handleGetRecommendations() {
        setStatus('Loading recommendations...');
        setRecommendations([]);

        try {
            const data = await getRecommendations(5);
            setRecommendations(data);
            setStatus(data.length === 0 ? 'No recommendations found.' : '');
        } catch (e) {
                setStatus(`Error: ${e.message}`);
        }
    }

return (
<div>
    <button onClick={handleGetRecommendations}>Get Recommendations</button>
    {status && <p>{status}</p>}

    <div className="rec-grid">
    {recommendations.map((rec) => (
        <div className="rec-card" key={rec.movie.imdbId}>
            <p> - </p>
        <img className="rec-poster" src={rec.movie.posterUrl} width = '200' height= '300' alt={rec.movie.title} />
        <div className="rec-info">
            <h3 className="rec-title">{rec.movie.title}</h3>
            <span className="rec-year">{rec.movie.year}</span>
            <span className="rec-score"> | Match score: {rec.score.toFixed(2)}</span>
            <p className="rec-meta">{rec.movie.genre}</p>
            <p className="rec-meta">Directed by {rec.movie.director}</p>
            <p> ------------------------------------------------------------ </p>
            <p> </p>
            <p> </p>
        </div>
        </div>
    ))}
    </div>
</div>
);
}

export default Recommendations;