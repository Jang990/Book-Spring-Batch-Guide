1. 단순 거래 파일(transaction.csv) 불러오기. 파일의 마지막 줄은 파일 총 거래 수를 알려준다.
2. 거래 정보를 거래 테이블에 저장한 후 잔액 반영.
3. 각 계좌 잔액을 나열하는 요약 파일(summary.csv) 생성.

* `ImportTransactionFileStep` : 거래정보 csv 파일 읽기 -> 읽어온 거래정보 저장
* `ApplayTransactionsStep` : 거래정보가 있는 계좌정보 불러오기 -> 거래 정보를 통해 잔액 최신화 -> DB 계좌 잔액 업데이트
* `GeneratedAccountSummaryStep` : 거래정보가 있는 계좌정보 불러오기 -> 불러온 정보를 바탕으로 csv 파일 만들기
