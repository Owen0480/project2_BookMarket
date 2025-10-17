package com.springboot.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BoardFormDto {
    private Long id;
    private String wrierid;
    private String writer;
    private String title;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    public Board toEntity() {
        Board build = Board.builder()
                .id(id)
                .writerid(wrierid)
                .writer(writer)
                .title(title)
                .content(content)
                .build();
        return build;
    }
    @Builder
    public BoardFormDto(Long id, String writerid, String writer, String title, String
            content, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.wrierid = writerid;
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }
}
