package com.portfolio.security;

import jakarta.servlet.*; import jakarta.servlet.http.*; import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; import org.springframework.security.core.context.SecurityContextHolder; import org.springframework.security.core.userdetails.UserDetails; import org.springframework.security.core.userdetails.UserDetailsService; import org.springframework.security.web.authentication.WebAuthenticationDetailsSource; import org.springframework.stereotype.Component; import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwt; private final UserDetailsService uds;
    public JwtAuthenticationFilter(JwtService jwt,UserDetailsService uds){this.jwt=jwt;this.uds=uds;}
    protected void doFilterInternal(HttpServletRequest req,HttpServletResponse res,FilterChain chain)throws ServletException,java.io.IOException{
        String h=req.getHeader("Authorization"); if(h!=null&&h.startsWith("Bearer ")){String token=h.substring(7); try{String username=jwt.extractUsername(token); if(username!=null&&SecurityContextHolder.getContext().getAuthentication()==null){UserDetails u=uds.loadUserByUsername(username); if(jwt.valid(token,u)){var a=new UsernamePasswordAuthenticationToken(u,null,u.getAuthorities()); a.setDetails(new WebAuthenticationDetailsSource().buildDetails(req)); SecurityContextHolder.getContext().setAuthentication(a);}}}catch(Exception ignored){}}
        chain.doFilter(req,res);
    }
}
