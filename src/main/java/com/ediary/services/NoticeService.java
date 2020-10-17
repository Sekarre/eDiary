package com.ediary.services;

import com.ediary.domain.Notice;
import com.ediary.domain.security.User;

import java.util.List;

public interface NoticeService {

    List<Notice> listNotices();
    Notice initNewNotice(User user);
    Notice addNotice(Notice notice);
}
