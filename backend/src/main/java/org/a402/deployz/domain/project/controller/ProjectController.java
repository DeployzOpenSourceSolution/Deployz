package org.a402.deployz.domain.project.controller;

import java.util.List;

import javax.validation.Valid;

import org.a402.deployz.domain.project.exception.PortNumberDuplicatedException;
import org.a402.deployz.domain.project.exception.PortNumberInconsistentException;
import org.a402.deployz.domain.project.exception.PortNumberOutOfRangeException;
import org.a402.deployz.domain.project.request.TotalProjectConfigRequest;
import org.a402.deployz.domain.project.response.ProjectResponse;
import org.a402.deployz.domain.project.service.ProjectService;
import org.a402.deployz.global.common.BaseResponse;
import org.a402.deployz.global.error.GlobalErrorCode;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

//	| orderList() | 목록 조회 유형의 서비스 |
//	| orderDetails() | 단 건 상세 조회 유형의 controller 메서드 |
//	| orderSave() | 등록/수정/삭제 가 동시에 일어나는 유형의 controller 메서드 |
//	| orderAdd() | 등록만 하는 유형의 controller 메서드 |
//	| orderModify() | 수정만 하는 유형의 controller 메서드 |
//	| orderRemove() | 삭제만 하는 유형의 controller 메서드 |

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/project")
@Tag(name = "Project", description = "프로젝트 관련 API")
@Slf4j
public class ProjectController {

	private final ProjectService projectService;

	@ApiResponse(responseCode = "200", description = "프로젝트 생성 성공")
	@Operation(description = "프로젝트 생성 API", summary = "프로젝트 생성 API")
	@PostMapping()
	public BaseResponse<Void> projectAdd(@Valid @RequestBody TotalProjectConfigRequest request,
		@AuthenticationPrincipal UserDetails userDetails) {
		projectService.addProject(request, userDetails);

		return new BaseResponse<>(GlobalErrorCode.SUCCESS);
	}

	@ApiResponse(responseCode = "200", description = "시크릿 토큰 생성 성공")
	@Operation(description = "시크릿 토큰 생성 API", summary = "시크릿 토큰 생성 API")
	@GetMapping("/git/secret-token")
	public BaseResponse<String> secretTokenCreate(@RequestParam String branchName,
		@AuthenticationPrincipal UserDetails userDetails) {
		final String secretToken = projectService.createSecretToken(branchName, userDetails.getUsername());

		return new BaseResponse<>(secretToken);
	}

	@ApiResponse(responseCode = "200", description = "프로젝트 삭제 성공")
	@Operation(description = "프로젝트 삭제 API", summary = "프로젝트 삭제 API")
	@DeleteMapping("/{projectIdx}")
	public BaseResponse<Void> projectRemove(@Valid @PathVariable Long projectIdx) {
		log.info("start remove project: {}", projectIdx);
		projectService.removeProject(projectIdx);

		return new BaseResponse<>(GlobalErrorCode.SUCCESS);
	}

	@ApiResponse(responseCode = "200", description = "프로젝트 목록 조회 성공")
	@Operation(description = "프로젝트 목록 조회 API", summary = "프로젝트 목록 조회 API")
	@GetMapping
	public BaseResponse<List<ProjectResponse>> findProjectList(@AuthenticationPrincipal UserDetails userDetails) {
		List<ProjectResponse> projectDtoList = projectService.findProjectList(userDetails.getUsername());

		return new BaseResponse<>(projectDtoList);
	}

	@ApiResponse(responseCode = "200", description = "프레임워크 타입 조회 성공")
	@Operation(description = "프레임워크 타입 조회 API", summary = "프레임워크 타입 조회 API")
	@GetMapping("/frameworkType")
	public BaseResponse<List<String>> frameworkTypeList() {
		List<String> frameworkTypes = projectService.findFrameworkTypeList();

		return new BaseResponse<>(frameworkTypes);
	}

	@ApiResponse(responseCode = "200", description = "빌드 버전 조회 성공")
	@Operation(description = "빌드 버전 조회 API", summary = "빌드 버전 조회 API")
	@GetMapping("/buildVersion/{frameworkType}")
	public BaseResponse<List<String>> buildVersionList(@PathVariable String frameworkType) {
		List<String> buildVersion = projectService.findBuildVersionList(frameworkType);

		return new BaseResponse<>(buildVersion);
	}

	@ApiResponse(responseCode = "200", description = "포트 중복검사 조회 성공")
	@Operation(description = "포드 번호 중복 검사 API", summary = "포드 번호 중복 검사 API")
	@GetMapping("/container")
	public BaseResponse<?> portCheckList(@RequestParam String port) {
		try {
			projectService.findPortNumCheckList(port);
			return new BaseResponse<>(GlobalErrorCode.SUCCESS);
		} catch (PortNumberInconsistentException e) {
			return new BaseResponse<>(GlobalErrorCode.INCONSISTENT_PORT_NUMBER);
		} catch (PortNumberOutOfRangeException e) {
			return new BaseResponse<>(GlobalErrorCode.OUT_OF_RANGE_PORT_NUM);
		} catch (PortNumberDuplicatedException e) {
			return new BaseResponse<>(GlobalErrorCode.DUPLICATE_PORT_NUMBER);
		}
	}
}
