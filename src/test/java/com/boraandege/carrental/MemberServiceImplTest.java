package com.boraandege.carrental;

import com.boraandege.carrental.dto.MemberDTO;
import com.boraandege.carrental.model.Member;
import com.boraandege.carrental.exception.BusinessException;
import com.boraandege.carrental.mapper.MemberMapper;
import com.boraandege.carrental.repository.MemberRepository;
import com.boraandege.carrental.service.impl.MemberServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MemberMapper memberMapper;

    @InjectMocks
    private MemberServiceImpl memberService;

    @Test
    void testRegisterMember_Success() {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setName("John Doe");
        memberDTO.setDrivingLicenseNumber("DL123");

        Member member = new Member();
        member.setName("John Doe");
        member.setDrivingLicenseNumber("DL123");

        Member savedMember = new Member();
        savedMember.setId(1L);
        savedMember.setName("John Doe");
        savedMember.setDrivingLicenseNumber("DL123");

        MemberDTO savedMemberDTO = new MemberDTO();
        savedMemberDTO.setId(1L);
        savedMemberDTO.setName("John Doe");
        savedMemberDTO.setDrivingLicenseNumber("DL123");

        when(memberRepository.existsByDrivingLicenseNumber("DL123")).thenReturn(false);
        when(memberMapper.toEntity(memberDTO)).thenReturn(member);
        when(memberRepository.save(member)).thenReturn(savedMember);
        when(memberMapper.toDTO(savedMember)).thenReturn(savedMemberDTO);

        MemberDTO result = memberService.registerMember(memberDTO);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        verify(memberRepository, times(1)).save(member);
        verify(memberMapper, times(1)).toEntity(memberDTO);
        verify(memberMapper, times(1)).toDTO(savedMember);
    }

    @Test
    void testRegisterMember_DuplicateLicense() {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setName("John Doe");
        memberDTO.setDrivingLicenseNumber("DL123");

        when(memberRepository.existsByDrivingLicenseNumber("DL123")).thenReturn(true);

        assertThrows(BusinessException.class, () -> memberService.registerMember(memberDTO));
        verify(memberRepository, never()).save(any(Member.class));
        verify(memberMapper, never()).toEntity(any());
    }

    @Test
    void testGetMemberById() {
        Member member = new Member();
        member.setId(1L);
        member.setName("John Doe");

        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setId(1L);
        memberDTO.setName("John Doe");

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(memberMapper.toDTO(member)).thenReturn(memberDTO);

        MemberDTO result = memberService.getMemberById(1L);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        verify(memberRepository, times(1)).findById(1L);
        verify(memberMapper, times(1)).toDTO(member);
    }

    @Test
    void testGetAllMembers() {
        Member member1 = new Member();
        member1.setId(1L);
        member1.setName("John Doe");

        Member member2 = new Member();
        member2.setId(2L);
        member2.setName("Jane Smith");

        MemberDTO memberDTO1 = new MemberDTO();
        memberDTO1.setId(1L);
        memberDTO1.setName("John Doe");

        MemberDTO memberDTO2 = new MemberDTO();
        memberDTO2.setId(2L);
        memberDTO2.setName("Jane Smith");

        when(memberRepository.findAll()).thenReturn(Arrays.asList(member1, member2));
        when(memberMapper.toDTO(member1)).thenReturn(memberDTO1);
        when(memberMapper.toDTO(member2)).thenReturn(memberDTO2);

        List<MemberDTO> result = memberService.getAllMembers();

        assertEquals(2, result.size());
        verify(memberRepository, times(1)).findAll();
        verify(memberMapper, times(1)).toDTO(member1);
        verify(memberMapper, times(1)).toDTO(member2);
    }

    @Test
    void testUpdateMember() {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setName("John Doe Updated");
        memberDTO.setEmail("john@example.com");

        Member existingMember = new Member();
        existingMember.setId(1L);
        existingMember.setName("John Doe");

        Member updatedMember = new Member();
        updatedMember.setId(1L);
        updatedMember.setName("John Doe Updated");
        updatedMember.setEmail("john@example.com");

        MemberDTO updatedMemberDTO = new MemberDTO();
        updatedMemberDTO.setId(1L);
        updatedMemberDTO.setName("John Doe Updated");
        updatedMemberDTO.setEmail("john@example.com");

        when(memberRepository.findById(1L)).thenReturn(Optional.of(existingMember));
        when(memberRepository.save(existingMember)).thenReturn(updatedMember);
        when(memberMapper.toDTO(updatedMember)).thenReturn(updatedMemberDTO);

        MemberDTO result = memberService.updateMember(1L, memberDTO);

        assertNotNull(result);
        assertEquals("John Doe Updated", result.getName());
        verify(memberRepository, times(1)).findById(1L);
        verify(memberRepository, times(1)).save(existingMember);
        verify(memberMapper, times(1)).toDTO(updatedMember);
    }

    @Test
    void testDeleteMember() {
        Member member = new Member();
        member.setId(1L);

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        memberService.deleteMember(1L);

        verify(memberRepository, times(1)).findById(1L);
        verify(memberRepository, times(1)).delete(member);
    }
}
