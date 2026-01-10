package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerexsys.controller;

import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerexsys.entity.Member;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerexsys.security.CustomUserDetails;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerexsys.service.MemberService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MemberController {

    final MemberService memberService;

    @GetMapping("/register")
    public String showRegister(Model model) {
        model.addAttribute("member", new Member());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("member") Member member,
                           BindingResult bindingResult,
                           Model model) {
        if(bindingResult.hasErrors()) {

            return "register";
        }
        try {
            memberService.createMember(member);
        } catch (RuntimeException e) {
            model.addAttribute("msg", "Đăng ký thất bại");
            return "register";
        }
        return "redirect:/login";
    }

    @GetMapping("/me")
    public String me(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        Member member = memberService.getMemberById(userDetails.getMember().getId());
        model.addAttribute("member", member);
        return "my-info";
    }

    @GetMapping("/update")
    public String showUpdateMyInfo(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        Member member = memberService.getMemberById(userDetails.getMember().getId());
        model.addAttribute("member", member);
        return "member-update-form";
    }


    @PostMapping("/update")
    public String updateMyInfo(@Valid @ModelAttribute("member") Member member,
                               BindingResult bindingResult,
                               Model model) {
        if(bindingResult.hasErrors()) {
            return "member-update-form";
        }
        try {
            memberService.updateMember(member);
        } catch (Exception e) {
            model.addAttribute("msg", "Cập nhật thất bại");
            return "member-update-form";
        }
        return "redirect:/me";
    }
}
