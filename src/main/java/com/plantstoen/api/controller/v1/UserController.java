package com.plantstoen.api.controller.v1;

import com.plantstoen.api.advice.exception.CUserNotFoundException;
import com.plantstoen.api.entity.User;
import com.plantstoen.api.model.response.CommonResult;
import com.plantstoen.api.model.response.ListResult;
import com.plantstoen.api.model.response.SingleResult;
import com.plantstoen.api.repo.UserJpaRepo;
import com.plantstoen.api.service.ResponseService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"2. User"})
@RequiredArgsConstructor // class 내부에 final 로 선언된 객체에 대해서 Constructor Injection 수행
@RestController // 결과 데이터를 JSON 으로 내보냄
@RequestMapping(value = "/v1") // API resource 를 버전별로 관리하기 위해 /v1 을 모든 리소스 주소에 적용되도록 처리
public class UserController {

    private final UserJpaRepo userJpaRepo;
    private final ResponseService responseService; // 결과를 처리할 Service

    @ApiImplicitParams({@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")})
    @ApiOperation(value = "회원 리스트 조회", notes = "모든 회원을 조회한다")
    @GetMapping(value = "/users")
    public ListResult<User> findAllUser() {
        return responseService.getListResult(userJpaRepo.findAll());
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")})
    @ApiOperation(value = "회원 단건 조회", notes = "userID로 회원을 조회한다")
    @GetMapping(value = "/user/{msrl}")
    public SingleResult<User> findUserById(@ApiParam(value = "회원 아이디", required = true)
                                           @PathVariable long msrl,
                                           @ApiParam(value = "언어", defaultValue = "ko")
                                           @RequestParam String lang) {
        return responseService.getSingleResult(userJpaRepo.findById(msrl).orElseThrow(CUserNotFoundException::new));
    }

    @ApiOperation(value = "회원 추가", notes = "회원을 추가한다.")
    @PostMapping(value = "/user")
    public SingleResult<User> save(@ApiParam(value = "회원 아이디", required = true)
                                   @RequestParam String userId,
                                   @ApiParam(value = "회원 이름", required = true)
                                   @RequestParam String name) {
        User user = User.builder().uid(userId).name(name).build();
        return responseService.getSingleResult(userJpaRepo.save(user));
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")})
    @ApiOperation(value = "회원 수정", notes = "회원정보를 수정한다")
    @PutMapping(value = "/user")
    public SingleResult<User> modify(@ApiParam(value = "회원 번호", required = true) @RequestParam long msrl,
                                     @ApiParam(value = "회원 아이디", required = true) @RequestParam String uid,
                                     @ApiParam(value = "회원 이름", required = true) @RequestParam String name) {
        User user = User.builder()
                .msrl(msrl)
                .uid(uid)
                .name(name)
                .build();
        return responseService.getSingleResult(userJpaRepo.save(user));
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")})
    @ApiOperation(value = "회원 삭제", notes = "userId로 회원정보를 삭제한다")
    @DeleteMapping(value = "/user/{msrl}")
    public CommonResult delete(@ApiParam(value = "회원번호", required = true) @PathVariable long msrl) {
        userJpaRepo.deleteById(msrl); // 성공 결과 정보만 필요한경우 getSuccessResult()를 이용
        return responseService.getSuccessResult();
    }
}
