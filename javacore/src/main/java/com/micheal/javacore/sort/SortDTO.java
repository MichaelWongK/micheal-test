package com.micheal.javacore.sort;

import lombok.Data;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2020/8/5 15:37
 * @Description
 */
@Data
public class SortDTO {

    private String sortTarget;

    public SortDTO(String sortTarget) {
        this.sortTarget = sortTarget;
    }



}
