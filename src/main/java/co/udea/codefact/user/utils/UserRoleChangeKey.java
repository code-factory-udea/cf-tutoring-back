package co.udea.codefact.user.utils;

public class UserRoleChangeKey {

        private Long oldRoleId;
        private Long newRoleId;
    
        public UserRoleChangeKey(Long oldRoleId, Long newRoleId) {
            this.oldRoleId = oldRoleId;
            this.newRoleId = newRoleId;
        }
    
        public Long getOldRoleId() {
            return oldRoleId;
        }
    
        public Long getNewRoleId() {
            return newRoleId;
        }
    
        @Override
        public boolean equals(Object obj) {
            if (obj instanceof UserRoleChangeKey key) {
                return this.oldRoleId.equals(key.oldRoleId) && this.newRoleId.equals(key.newRoleId);
            }
            return false;
        }
    
        @Override
        public int hashCode() {
            return this.oldRoleId.hashCode() + this.newRoleId.hashCode();
        }
    }
    
