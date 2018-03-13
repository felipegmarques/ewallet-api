(ns ewallet-api.e2e-test
    (:import java.util.Date)
    (:import java.text.SimpleDateFormat)
  (:require [clojure.test :refer :all]
            [ewallet-api.api :refer :all]))

(def date-parser (SimpleDateFormat. "yyyy/MM/dd"))

(def entries 
    [
        {:value -100.0 :date (.parse date-parser "2015/05/12") :description "Sorvete"},
        {:value -50.0 :date (.parse date-parser "2015/05/15") :description "Kebab"},
    ])

(def start-date (.parse date-parser "2015/05/01"))
(def end-date (.parse date-parser "2015/05/31"))

(def test-dir (str (System/getProperty "user.dir") "/.env/test-db/"))

(deftest get-entries-by-period-test
    (testing "Get Entries for Date"
    (is (= 
        entries
        (get-entries-by-period (str test-dir "entries.csv") start-date end-date)))))

(def summary
    {:current-amount 50.0 :previous-amount 200.0 :debits -150.0 :credits 0.})

(deftest get-summary-by-period-test
    (testing "Get summary for period"
    (is (=
        summary
        (get-summary-by-period (str test-dir "entries.csv") start-date end-date)))))

(deftest save-entry-test
    (testing "Save entry to entries"
    (is (=
        (slurp (str test-dir "expected-entries.csv"))
        (do
            (save-entry 
                { :value 100.0 :date (.parse date-parser "2015/05/03") :description "Kebab"}
                (str test-dir "entries.csv")
                (str test-dir "new-entries.csv"))
            (slurp (str test-dir "new-entries.csv")))))))