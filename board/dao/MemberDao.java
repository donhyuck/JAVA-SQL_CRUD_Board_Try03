package board.dao;

import java.sql.Connection;
import java.util.Map;

import board.dto.Member;
import board.util.DBUtil;
import board.util.SecSql;

public class MemberDao {

	Connection conn;

	public MemberDao(Connection conn) {
		this.conn = conn;
	}

	public int getMemberCntByLoginId(String loginId) {

		SecSql sql = new SecSql();

		// DB에 등록된 아이디를 조회
		sql.append("SELECT COUNT(*) FROM `member`");
		sql.append("WHERE loginId = ?", loginId);

		return DBUtil.selectRowIntValue(conn, sql);

	}

	public Member getMemberByLoginId(String loginId) {

		SecSql sql = new SecSql();
		sql.append("SELECT * FROM `member`");
		sql.append("WHERE loginId = ?", loginId);

		Map<String, Object> memberMap = DBUtil.selectRow(conn, sql);

		return new Member(memberMap);
	}

	public void doJoin(String loginId, String loginPw, String name) {

		SecSql sql = new SecSql();

		sql.append("INSERT INTO `member`");
		sql.append("SET regDate = NOW()");
		sql.append(", updateDate = NOW()");
		sql.append(", loginId = ?", loginId);
		sql.append(", loginPw = ?", loginPw);
		sql.append(", name = ?", name);

		DBUtil.insert(conn, sql);
	}
}
