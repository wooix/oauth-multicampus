package com.multi.contactsapp.dao;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.multi.contactsapp.domain.Contact;

@Repository
public class ContactDAO {
	@Autowired
	SqlSession sqlSession;

	public List<Contact> getContactList() {
		return sqlSession.selectList("contact.selectall");
	}

	public List<Contact> getContactList(int pageNo, int pageSize) {
		int offset = (pageNo - 1) * pageSize;
		return sqlSession.selectList("contact.selectall", null, new RowBounds(offset, pageSize));
	}

	public long insertContact(Contact contact) {
		sqlSession.insert("contact.insert", contact);
		return contact.getNo();
	}

	public int getContactCount() {
		return sqlSession.selectOne("contact.selectcnt");
	}

	public Contact getContactOne(Contact c) {
		return sqlSession.selectOne("contact.selectone", c);
	}

	public int updateContact(Contact c) {
		return sqlSession.update("contact.update", c);
	}

	public int deleteContact(Contact c) {
		return sqlSession.delete("contact.delete", c);
	}

}
