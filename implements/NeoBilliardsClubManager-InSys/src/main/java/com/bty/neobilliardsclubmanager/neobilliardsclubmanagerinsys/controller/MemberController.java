package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.controller;

import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.response.MemberResponse;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.service.MemberService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MemberController {

    final MemberService memberService;

    @GetMapping("/members")
    public String getMembers(@RequestParam(name = "key", required = false, defaultValue = "") String key,
                             Model model) {
        List<MemberResponse> memberResponses = null;
        if(key.isEmpty()) {

            memberResponses = memberService.getAllMembers();
        } else {
            memberResponses = memberService.findMembersByFullNameContaining(key);
        }
        model.addAttribute("memberResponses", memberResponses);
        return "members";
    }

    @GetMapping("/members/lock/{id}")
    public String lockMember(@PathVariable(name = "id") Long id) {
        memberService.lockMember(id);
        return "redirect:/members";
    }

    @GetMapping("/members/unlock/{id}")
    public String unlockMember(@PathVariable(name = "id") Long id) {
        memberService.unlockMember(id);
        return "redirect:/members";
    }
}
