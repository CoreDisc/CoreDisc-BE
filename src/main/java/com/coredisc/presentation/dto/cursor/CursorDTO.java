package com.coredisc.presentation.dto.cursor;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CursorDTO<T> {

    private List<T> values;

    private Boolean hasNext;
}
