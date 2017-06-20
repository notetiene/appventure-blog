;;; list.clj --- List section.
;; Keywords: menu, section, list, template
;; Last-Updated: Tue Jun 20 17:00:26 (EDT) 2017 by etienne
;; Created: 2017-06-20

;; License: Eclipse Public License

;; Copyright (c) 2017 Etienne Prud’homme

;; All rights reserved. This program and the accompanying materials
;; are made available under the terms of the Eclipse Public License
;; v1.0 which accompanies this distribution, and is available at
;; http://www.eclipse.org/legal/epl-v10.html

;;; Commentary:
;;; Code:


(static.core/define-template base-template-file
  [:head] (enlive/content
           (static.core/template-head-model metadata {}))

  [:#maincontent] (enlive/content
                   (map #(static.core/template-tags-model %)
                        content))

                                        ; The categories
  [:#categories] (enlive/content
                  (map #(static.core/template-category-model %)
                       (:categories metadata)))

  [:#swiftblogs] (enlive/content nil)

                                        ; And the projects
  [:#projects] (enlive/content
                (map #(template-project-model %)
                     (:projects metadata))))

;;; list.clj<templates> ends here

;; Local Variables:
;; coding: utf-8
;; End:
