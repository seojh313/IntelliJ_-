package kr.ac.kopo.board.controller;

import kr.ac.kopo.board.dto.BoardDTO;
import kr.ac.kopo.board.dto.PageRequestDTO;
import kr.ac.kopo.board.dto.PageResultDTO;
import kr.ac.kopo.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // /board/에 대한 요청 처리
    @GetMapping("/") // 슬래시 포함 경로
    public String index() {
        return "board/index"; // board/index.html 반환
    }

    @GetMapping // 기본 경로
    public String redirectToBoard() {
        return "redirect:/board/"; // /board로 접근 시 /board/로 리다이렉트
    }




    // 리스트 페이지로 이동
    @GetMapping("/list.html")
    public String list(PageRequestDTO pageRequestDTO, Model model) {
        // PageRequestDTO를 전달하여 페이지 결과를 얻음
        PageResultDTO<BoardDTO, Object[]> result = boardService.getList(pageRequestDTO);
        model.addAttribute("result", result); // 결과를 모델에 추가하여 뷰로 전달
        return "board/list"; // board/list.html 반환
    }

    // 등록 페이지로 이동
    @GetMapping("/register")
    public void register() {
    }

    // 등록 처리
    @PostMapping("/register")
    public String registerPost(BoardDTO dto, RedirectAttributes redirectAttributes) {
        Long bno = boardService.register(dto);
        redirectAttributes.addFlashAttribute("msg", bno);
        return "redirect:/board/list.html"; // 등록 후 리스트 페이지로 리다이렉트
    }

    // 읽기 및 수정 페이지로 이동
    @GetMapping({"/read", "/modify"})
    public String read(@ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO, Long bno, Model model) {
        BoardDTO boardDTO = boardService.get(bno);
        model.addAttribute("dto", boardDTO); // 게시글 데이터 모델에 추가
        return "board/read"; // board/read.html 반환
    }

    // 수정 처리
    @PostMapping("/modify")
    public String modify(BoardDTO dto, @ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO, RedirectAttributes redirectAttributes) {
        boardService.modify(dto);
        redirectAttributes.addAttribute("page", pageRequestDTO.getPage());
        redirectAttributes.addAttribute("type", pageRequestDTO.getType());
        redirectAttributes.addAttribute("keyword", pageRequestDTO.getKeyword());
        redirectAttributes.addAttribute("bno", dto.getBno());
        return "redirect:/board/read"; // 수정 후 읽기 페이지로 리다이렉트
    }

    // 삭제 처리
    @PostMapping("/remove")
    public String remove(long bno, RedirectAttributes redirectAttributes) {
        boardService.removeWithReplies(bno);
        redirectAttributes.addFlashAttribute("msg", bno);
        return "redirect:/board/list"; // 삭제 후 리스트 페이지로 리다이렉트
    }

    // generic.html 반환 메서드
    @GetMapping("/generic.html")
    public String generic() {
        return "board/generic"; // board/generic.html 반환
    }

    // elements.html 반환 메서드
    @GetMapping("/elements.html")
    public String elements() {
        return "board/elements"; // board/elements.html 반환
    }

}
