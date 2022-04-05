import H3 from 'assets/theme/Typography/H3/H3';
import H4 from 'assets/theme/Typography/H4/H4';
import P from 'assets/theme/Typography/P/P';
import classNames from 'classnames/bind';
import { useNavigate } from 'react-router-dom';
// import { DonationDetailType } from '../DontateDetail/DonateDetail';
import styles from './UserInfo.module.scss';
import { ReactComponent as ErrorIcon } from 'assets/images/icon/error.svg';
import { DontationDetailType } from 'types/DonationTypes';

const cx = classNames.bind(styles);

type UserInfoProps = {
  data: DontationDetailType;
};
const UserInfo = ({ data }: UserInfoProps) => {
  const navigate = useNavigate();

  const handleNavigate = () => {
    navigate(`/profile/${data.nickname}`);
  };
  return (
    <div className={cx('inner-container')}>
      <div className={cx('title')}>
        <H3>신청자 정보</H3>
      </div>
      <div>
        {data.deputy ? (
          <div className={cx('alert-message')}>
            <div className={cx('icon')}>
              <ErrorIcon />
            </div>
            <P color="red">
              이 모금은 모금 대상자의 대리인이 신청한 모금입니다.
            </P>
          </div>
        ) : null}
      </div>
      <div className={cx('info')}>
        <dl>
          <dt>성명(주민등록상 이름)</dt>
          <dd>{data.name}</dd>
        </dl>
        <dl>
          <dt>이메일</dt>
          <dd>{data.email}</dd>
        </dl>
        <dl>
          <dt>기존 모금 신청 여부</dt>
          <dd>
            <P color="orange">{data.exist ? '이력 존재' : '이력 미존재'}</P>
          </dd>
        </dl>
      </div>
      <div className={cx('profile-btn')}>
        <button onClick={handleNavigate}>프로필 이동</button>
      </div>
    </div>
  );
};

export default UserInfo;