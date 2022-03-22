package com.doners.donersbackend.api.service;

import com.doners.donersbackend.api.dto.request.AppreciationChangePatchDTO;
import com.doners.donersbackend.api.dto.request.AppreciationRegisterPostDTO;
import com.doners.donersbackend.api.dto.response.*;
import com.doners.donersbackend.db.entity.Appreciation;
import com.doners.donersbackend.db.entity.AppreciationBudget;
import com.doners.donersbackend.db.entity.Comment;
import com.doners.donersbackend.db.entity.Community;
import com.doners.donersbackend.db.entity.donation.DonationBudget;
import com.doners.donersbackend.db.repository.AppreciationBudgetRepository;
import com.doners.donersbackend.db.repository.AppreciationRepository;
import com.doners.donersbackend.db.repository.CommentRepository;
import com.doners.donersbackend.db.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppreciationServiceImpl implements AppreciationService{

    private final AppreciationRepository appreciationRepository;
    private final AppreciationBudgetRepository appreciationBudgetRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    // 글 등록 : 필수 글 정보 입력 - 제목, 내용, 작성자
    @Override
    public void appreciationRegister(AppreciationRegisterPostDTO appreciationRegisterPostDTO) {
        // 글작성 정보 추가할 것
        Appreciation appreciation = Appreciation.builder()
                .appreciationTitle(appreciationRegisterPostDTO.getAppreciationTitle())
                .appreciationDescription(appreciationRegisterPostDTO.getAppreciationDescription())
                .user(userRepository.findByUserAccount(appreciationRegisterPostDTO.getUserAccount()).get())
                .appreciationCreateTime(LocalDateTime.now()).build();

        // 활동 내역 추가
        appreciationRegisterPostDTO.getAppreciationBudgetRequestDTOList().forEach(appreciationBudgetRequestDTO ->
                appreciationBudgetRepository.save(
                        AppreciationBudget.builder()
                                .appreciationBudgetPlan(appreciationBudgetRequestDTO.getAppreciationBudgetPlan())
                                .appreciationBudgetAmount(appreciationBudgetRequestDTO.getAppreciationBudgetAmount())
                                .appreciation(appreciation)
                                .build()
                )
        );
        appreciationRepository.save(appreciation);
    }

    @Override
    public Integer changeAppreciation(String appreciationId, AppreciationChangePatchDTO appreciationChangePatchDTO) {
        Appreciation appreciation = appreciationRepository.findById(appreciationId)
                .orElseThrow(() -> new IllegalArgumentException("해당 글을 찾을 수 없습니다."));

        try {
            appreciation.changeAppreciation(appreciationChangePatchDTO.getAppreciationTitle(),appreciationChangePatchDTO.getAppreciationDescription());
        } catch(Exception e) {
            return 409;
        }

        appreciationRepository.save(appreciation);
        return 200;
    }

    @Override
    public Integer deleteAppreciation(String appreciationId) {
        Appreciation appreciation = appreciationRepository.findById(appreciationId)
                .orElseThrow(() -> new IllegalArgumentException("해당 글을 찾을 수 없습니다."));

        try {
            appreciation.deleteAppreciation();
        } catch(Exception e) {
            return 409;
        }

        appreciationRepository.save(appreciation);
        return 200;
    }

    @Override
    public AppreciationGetListWrapperResponseDTO getAppreciationList() {
        List<Appreciation> appreciationList = appreciationRepository.findByAppreciationIsDeleted(false).orElse(null);

        List<AppreciationGetListResponseDTO> appreciationGetListResponseDTOList = new ArrayList<>();

        appreciationList.forEach(appreciation -> {
            appreciationGetListResponseDTOList.add(
                    AppreciationGetListResponseDTO.builder()
                            .appreciationId(appreciation.getId())
                            .appreciationTitle(appreciation.getAppreciationTitle())
                            .appreciationDescription(appreciation.getAppreciationDescription())
                            .appreciationCreateTime(appreciation.getAppreciationCreateTime())
                            .appreciationViews(appreciation.getAppreciationViews())
                            .appreciationWriter(appreciation.getUser().getUserNickname())
                            .build()
            );
        });

        return AppreciationGetListWrapperResponseDTO.builder()
                .appreciationGetListResponseDTOList(appreciationGetListResponseDTOList)
                .build();
    }

    @Override
    public AppreciationResponseDTO getAppreciation(String appreciationId) {
        Appreciation appreciation = appreciationRepository.findById(appreciationId)
                .orElseThrow(() -> new IllegalArgumentException("해당 감사 글을 찾을 수 없습니다."));

        List<AppreciationBudget> appreciationBudgetList = appreciationBudgetRepository.findAllByAppreciation(appreciation)
                .orElse(null); // 사용 내역이 없는 경우
        List<AppreciationBudgetResponseDTO> appreciationBudgetResponseDTOList = new ArrayList<>();

        appreciationBudgetList.forEach(appreciationBudget ->
                appreciationBudgetResponseDTOList.add(
                        AppreciationBudgetResponseDTO.builder()
                                .appreciationBudgetPlan(appreciationBudget.getAppreciationBudgetPlan())
                                .appreciationBudgetAmount(appreciationBudget.getAppreciationBudgetAmount())
                                .build()
                )
        );

        List<Comment> commentList = commentRepository.findAllByAppreciation(appreciation)
                .orElse(null);// 댓글이 없는 경우
        List<CommentResponseDTO> commentResponseDTOList = new ArrayList<>();

        commentList.forEach(comment ->
                commentResponseDTOList.add(
                        CommentResponseDTO.builder()
                                .commentId(comment.getId())
                                .commentCreateTime(comment.getCommentCreateTime())
                                .commentDescription(comment.getCommentDescription())
                                .build()
                )
        );
        increaseViews(appreciation);
        return AppreciationResponseDTO.builder()
                .appreciationTitle(appreciation.getAppreciationTitle())
                .appreciationDescription(appreciation.getAppreciationDescription())
                .appreciationCreateTime(appreciation.getAppreciationCreateTime())
                .appreciationViews(appreciation.getAppreciationViews())
                .commentResponseDTOList(commentResponseDTOList)
                .appreciationWriter(appreciation.getUser().getUserNickname())
                .build();
    }
    
    public void increaseViews(Appreciation appreciation) {
        // 조회수 업데이트
        appreciation.updateViews();

        appreciationRepository.save(appreciation);
    }
}