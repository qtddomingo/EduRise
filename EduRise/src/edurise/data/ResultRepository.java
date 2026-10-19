package edurise.data;

import edurise.model.QuizResult;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ResultRepository extends BaseRepository<QuizResult> {

    @Override protected String table() { return "results"; }

    @Override
    protected QuizResult map(ResultSet rs) throws SQLException {
        return new QuizResult(rs.getInt("id"), rs.getInt("user_id"), rs.getString("topic"),
                rs.getInt("stage"), rs.getInt("correct"), rs.getInt("total"),
                rs.getInt("score"), rs.getString("taken_at"));
    }

    @Override
    public boolean save(QuizResult r) {
        return update("INSERT INTO results(user_id, topic, stage, correct, total, score, taken_at) VALUES(?,?,?,?,?,?,?)",
                r.getUserId(), r.getTopic(), r.getStage(), r.getCorrect(), r.getTotal(), r.getScore(), r.getTakenAt());
    }

    public List<QuizResult> findByUser(int userId) {
        return query("SELECT * FROM results WHERE user_id = ? ORDER BY id", userId);
    }
}
