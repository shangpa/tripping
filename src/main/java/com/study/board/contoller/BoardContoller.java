package com.study.board.contoller;
import com.study.board.entity.Board;
import com.study.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;
@Controller
public class BoardContoller {
    @Autowired
    private BoardService boardService;


    @GetMapping("/board/write")
    public String boardWriteForm(){


        return "boardwrite";
    }

    @PostMapping("/board/writepro")
    public String boardWritePro(Board board){
        boardService.write(board);
        return "";
    }

    @GetMapping("/board/list")
    public  String boardList(Model model){
        model.addAttribute("list",boardService.boardList());
        return "boardList";
    }

    @GetMapping("/board/view")
    public String boardView(Model model, @RequestParam("id") int id) {
        model.addAttribute("board", boardService.boardView(id));
        // localhost/board/view?id=1
        return "boardView"; // 뷰를 담당하는 템플릿 파일 이름 리턴에 써주면 된다..
    }

}
