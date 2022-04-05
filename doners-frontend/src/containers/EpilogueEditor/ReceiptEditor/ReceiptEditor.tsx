import Button from 'assets/theme/Button/Button';
import Input from 'assets/theme/Input/Input';
import P from 'assets/theme/Typography/P/P';

import classNames from 'classnames/bind';
import styles from './ReceiptEditor.module.scss';
import HistoryItem from 'components/HistoryItem/HistoryItem';
import { useEffect, useRef, useState } from 'react';

const cx = classNames.bind(styles);

type historyType = {
  epilogueBudgetSequence: number;
  epilogueBudgetPlan: string;
  epilogueBudgetAmount: string;
};

const ReceiptEditor = ({ onDelete, onChange, list }: any) => {
  const [historyList, setHistoryList] = useState<historyType[]>([]);
  const [history, setHistory] = useState('');
  const [money, setMoney] = useState('');
  const Id = useRef<number>(0);
  const handleOnclick = () => {
    if (history && money) {
      const data = {
        epilogueBudgetPlan: history,
        epilogueBudgetAmount: money,
      };
      setHistoryList((prev) => [
        ...prev,
        {
          epilogueBudgetSequence: Id.current,
          epilogueBudgetPlan: history,
          epilogueBudgetAmount: money,
        },
      ]);
      onChange({
        epilogueBudgetSequence: Id.current++,
        epilogueBudgetPlan: history,
        epilogueBudgetAmount: money,
      });
      setHistory('');
      setMoney('');
    }
  };

  const handleHistoryDelete = (epilogueBudgetSequence: number): void => {
    console.log(epilogueBudgetSequence);
    setHistoryList(
      historyList.filter(
        (history) => history.epilogueBudgetSequence !== epilogueBudgetSequence
      )
    );
    onDelete(epilogueBudgetSequence);
  };

  useEffect(() => {
    // console.log('여긴가???');
    for (let id in list) {
      console.log(id);
      console.log(list[id]);
      // setHistoryList((prev) => [
      //   ...prev,
      //   {
      //     id,
      //     ...list[id],
      //   },
      // ]);
    }
    // setHistoryList(list);
  }, [list]);

  useEffect(() => {
    console.log(historyList);
  }, [historyList]);

  const total = historyList
    .map((item) => Number(item.epilogueBudgetAmount))
    .reduce((prev, curr) => prev + curr, 0);

  return (
    <div className={cx('receipt-editor')}>
      <div className={cx('input-form')}>
        <div className={cx('input-history')}>
          <Input
            placeholder="활용 내역"
            onChange={(ev) => setHistory(ev.target.value)}
            value={history}
          />
        </div>
        <div className={cx('input-value')}>
          <Input
            placeholder="KRW"
            onChange={(ev) => setMoney(ev.target.value)}
            value={money}
            type="number"
          />
        </div>
        <div className={cx('add-btn')}>
          <Button color="secondary" fullWidth onClick={handleOnclick}>
            추가
          </Button>
        </div>
      </div>
      <div className={cx('history-list')}>
        {historyList.map((data, idx) => {
          return (
            <HistoryItem
              value={data}
              key={idx}
              onDelete={handleHistoryDelete}
            />
          );
        })}
      </div>
      <div className={cx('total-use-value')}>
        <P>{`총 사용 모금액: ${total.toLocaleString()} KRW`}</P>
      </div>
    </div>
  );
};

export default ReceiptEditor;