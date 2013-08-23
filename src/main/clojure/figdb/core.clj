(ns figdb.core)
(import 'org.hibernate.SessionFactory
  'org.hibernate.cfg.Configuration)

(defonce session-factory (delay (-> (Configuration.)
                                  .configure .buildSessionFactory)))

(defn get-person []
  (with-open [session (.openSession @session-factory)]
    (-> session
    (.createQuery "from Individual where uecode='00699946000'") .list)))
