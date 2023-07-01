package com.yan.yygh.common.exphander;

import com.yan.yygh.common.result.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    @ResponseBody
    public R error(Exception e){
        e.printStackTrace();
        return R.error();
    }

    @ExceptionHandler
    @ResponseBody
    public R error(ArithmeticException e){
        e.printStackTrace();
        return R.error().message("执行了特定异常");
    }

    @ExceptionHandler
    @ResponseBody
    public R error(NullPointerException e){
        e.printStackTrace();
        return R.error().message("空指针异常");
    }

    @ExceptionHandler(YyghException.class)
    @ResponseBody
    public R error(YyghException e){
        log.error(e.getMessage());
        return R.error().message(e.getMsg()).code(e.getCode());
    }
}
