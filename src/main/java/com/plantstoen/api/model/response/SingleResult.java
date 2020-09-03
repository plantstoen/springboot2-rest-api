package com.plantstoen.api.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SingleResult<T> extends CommonResult {
    // Generic Interface 에 <T> 를 지정하여 어떤 형태의 값도 넣을 수 있도록 구현
    private T data;
}
