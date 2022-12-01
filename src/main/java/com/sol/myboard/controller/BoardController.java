package com.sol.myboard.controller;

import com.sol.myboard.model.Board;
import com.sol.myboard.repository.BoardRepository;
import com.sol.myboard.service.BoardService;
import com.sol.myboard.validator.BoardValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardValidator boardValidator;

    @GetMapping("/list")
    public String list(Model model, @PageableDefault(size = 5)  Pageable pageable){

        // spring.io > spring-data > 'pagable'
        Page<Board> boards = boardRepository.findAll(pageable);
        //boards.getTotalElements();
        int startPage = Math.max(1, boards.getPageable().getPageNumber() - 4);
        int endPage = Math.min(boards.getTotalPages(), boards.getPageable().getPageNumber() + 4);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("boards", boards);
        return "board/list";
    }

    //READ
    @GetMapping("/read")
    public String read(Model model, @RequestParam(required = true) Long id){
        Board board = boardRepository.findById(id).orElse(null);
        model.addAttribute("board", board);
        return "board/read";
    }

    //UPDATE
    @GetMapping("/form")
    public String form(Model model, @RequestParam(required = false) Long id){
        if(id == null){
            model.addAttribute("board", new Board());
        } else {
            Board board = boardRepository.findById(id).orElse(null);
            model.addAttribute("board", board);
        }
        return "board/form";
    }
    @PostMapping("/form")
    public String boardSubmit(@Valid Board board, BindingResult bindingResult, Authentication authentication){
        boardValidator.validate(board, bindingResult);
        if (bindingResult.hasErrors()) {
            return "/board/form";
        };
        String username = authentication.getName();
        boardService.save(username, board);
        //boardRepository.save(board);
        return "redirect:/board/list";
    }

    //DELETE Api
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/delete/{id}")
    public String deleteBoard(@PathVariable Long id) {
        boardRepository.deleteById(id);
        return "redirect:/board/list";
    }
}
