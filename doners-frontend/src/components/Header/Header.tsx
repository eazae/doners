/* eslint-disable react-hooks/exhaustive-deps */
import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import H5 from 'assets/theme/Typography/H5/H5';
import styles from './Header.module.scss';
import classNames from 'classnames/bind';
import Button from 'assets/theme/Button/Button';
import Logo from 'assets/images/header-logo.svg';
import { getLoggedUserInfo } from 'utils/loggedUser';
import { useRecoilValue } from 'recoil';
import { isLoggedState } from '../../atoms/atoms';

const cx = classNames.bind(styles);
const Header = () => {
  const [loggedUserInfo, setLoggedUserInfo] = useState(false);
  const [ScrollY, setScrollY] = useState(0); // window 의 pageYOffset값을 저장
  const [ScrollActive, setScrollActive] = useState(false);

  function handleScroll() {
    if (ScrollY > 45) {
      setScrollY(window.pageYOffset);
      setScrollActive(true);
    } else {
      setScrollY(window.pageYOffset);
      setScrollActive(false);
    }
  }

  useEffect(() => {
    function scrollListener() {
      window.addEventListener('scroll', handleScroll);
    } //  window 에서 스크롤을 감시 시작
    scrollListener(); // window 에서 스크롤을 감시
    return () => {
      window.removeEventListener('scroll', handleScroll);
    }; //  window 에서 스크롤을 감시를 종료
  });

  useEffect(() => {
    const localStorageUserInfo = getLoggedUserInfo();
    console.log(localStorageUserInfo);
    if (localStorageUserInfo) setLoggedUserInfo(true);
  }, [getLoggedUserInfo()]);

  return (
    <div
      className={
        ScrollActive
          ? [styles.header, styles.fixed].join(' ')
          : styles['header']
      }
    >
      <div className={cx('header-leftside')}>
        <ul className={cx('header-list')}>
          <Link to="/">
            <li>
              <H5>서비스 소개</H5>
            </li>
          </Link>
          <li>
            <H5>기부신청</H5>
          </li>
          <Link to="/category">
            <li>
              <H5>기부하기</H5>
            </li>
          </Link>
          <Link to="/community">
            <li>
              <H5>커뮤니티</H5>
            </li>
          </Link>
        </ul>
      </div>
      <div className={cx('header-center')}>
        <div className={styles.logo}>
          <Link to="/">
            <img src={Logo} className={styles.logoImg} alt="logo" />
          </Link>
        </div>
      </div>
      <div className={cx('header-rightside')}>
        <ul className={cx('header-list')}>
          <li>
            <H5>언어</H5>
          </li>
          <li>
            <H5>알림</H5>
          </li>
          <li>
            <H5>프로필</H5>
          </li>
          <div className="btn">
            {loggedUserInfo ? (
              <Link to="/profile">
                <Button size="small" fullWidth color={'alternate'}>
                  Profile
                </Button>
              </Link>
            ) : (
              <Link to="/signup">
                <Button size="small" fullWidth color={'alternate'}>
                  Join
                </Button>
              </Link>
            )}
          </div>
        </ul>
      </div>
    </div>
  );
};

export default Header;