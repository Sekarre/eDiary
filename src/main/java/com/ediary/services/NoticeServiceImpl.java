package com.ediary.services;

import com.ediary.domain.Notice;
import com.ediary.repositories.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;

    @Override
    public List<Notice> listNotices() {
        return noticeRepository.findAll();
    }
}
