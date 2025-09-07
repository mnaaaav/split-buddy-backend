document.addEventListener('DOMContentLoaded', () => {
  const groupSelect = document.getElementById('groupSelect');
  const membersTable = document.getElementById('membersTable');
  const expensesTable = document.getElementById('expensesTable');
  const summaryTable = document.getElementById('summaryTable');
  const addUserForm = document.getElementById('addUserForm');
  const addGroupForm = document.getElementById('addGroupForm');
  const addMemberForm = document.getElementById('addMemberForm');
  const addExpenseForm = document.getElementById('addExpenseForm');

  // Fetch groups
  function loadGroups() {
    axios.get('http://localhost:9090/groups')
      .then(response => {
        groupSelect.innerHTML = '<option value="">Select a group</option>';
        response.data.forEach(group => {
          const option = document.createElement('option');
          option.value = group.id;
          option.textContent = group.name;
          groupSelect.appendChild(option);
        });
      })
      .catch(error => {
        console.error('Error fetching groups:', error);
        alert('Failed to load groups. Ensure Group Service is running at http://localhost:9090.');
      });
  }
  loadGroups();

  // Add User
  addUserForm.addEventListener('submit', (e) => {
    e.preventDefault();
    const user = {
      name: document.getElementById('userName').value,
      email: document.getElementById('userEmail').value,
      password: document.getElementById('userPassword').value
    };
    axios.post('http://localhost:9090/users', user)
      .then(() => {
        alert('User added!');
        addUserForm.reset();
      })
      .catch(error => {
        console.error('Error adding user:', error);
        alert('Failed to add user.');
      });
  });

  // Add Group
  addGroupForm.addEventListener('submit', (e) => {
    e.preventDefault();
    const group = { name: document.getElementById('groupName').value };
    const userId = document.getElementById('groupUserId').value;
    axios.post(`http://localhost:9090/groups?userId=${userId}`, group)
      .then(() => {
        alert('Group added!');
        addGroupForm.reset();
        loadGroups();
      })
      .catch(error => {
        console.error('Error adding group:', error);
        alert('Failed to add group.');
      });
  });

  // Add Member
  addMemberForm.addEventListener('submit', (e) => {
    e.preventDefault();
    const groupId = groupSelect.value;
    const userId = document.getElementById('memberUserId').value;
    if (!groupId) {
      alert('Please select a group.');
      return;
    }
    axios.post(`http://localhost:9090/groups/${groupId}/members?userId=${userId}`)
      .then(() => {
        alert('Member added!');
        addMemberForm.reset();
        loadMembers(groupId);
      })
      .catch(error => {
        console.error('Error adding member:', error);
        alert('Failed to add member.');
      });
  });

  // Add Expense
  addExpenseForm.addEventListener('submit', (e) => {
    e.preventDefault();
    const groupId = groupSelect.value;
    if (!groupId) {
      alert('Please select a group.');
      return;
    }
    const expense = {
      groupId: parseInt(groupId),
      paidByUserId: parseInt(document.getElementById('expenseUserId').value),
      amount: parseFloat(document.getElementById('expenseAmount').value),
      description: document.getElementById('expenseDescription').value
    };
    axios.post('http://localhost:8082/settlements/expenses', expense)
      .then(() => {
        alert('Expense added!');
        addExpenseForm.reset();
        loadExpenses(groupId);
        loadSummary(groupId);
      })
      .catch(error => {
        console.error('Error adding expense:', error);
        alert('Failed to add expense.');
      });
  });

  // Load Members
  function loadMembers(groupId) {
    axios.get(`http://localhost:9090/groups/${groupId}/members`)
      .then(response => {
        membersTable.innerHTML = '';
        response.data.forEach(member => {
          const row = document.createElement('tr');
          row.innerHTML = `<td>${member.id}</td><td>${member.name}</td><td>${member.email}</td>`;
          membersTable.appendChild(row);
        });
      })
      .catch(error => console.error('Error fetching members:', error));
  }

  // Load Expenses
  function loadExpenses(groupId) {
    axios.get(`http://localhost:8082/settlements/group/${groupId}/expenses`)
      .then(response => {
        expensesTable.innerHTML = '';
        response.data.forEach(expense => {
          const row = document.createElement('tr');
          row.innerHTML = `<td>${expense.id}</td><td>${expense.paidByUserId}</td><td>$${expense.amount.toFixed(2)}</td><td>${expense.description}</td>`;
          expensesTable.appendChild(row);
        });
      })
      .catch(error => console.error('Error fetching expenses:', error));
  }

  // Load Summary
  function loadSummary(groupId) {
    axios.get(`http://localhost:8082/settlements/group/${groupId}/summary`)
      .then(response => {
        summaryTable.innerHTML = '';
        response.data.forEach(item => {
          const row = document.createElement('tr');
          row.innerHTML = `<td>${item.userId}</td><td>${item.name}</td><td>${item.email}</td><td>$${item.paid.toFixed(2)}</td><td>$${item.fairShare.toFixed(2)}</td><td>$${item.balance.toFixed(2)}</td>`;
          summaryTable.appendChild(row);
        });
      })
      .catch(error => console.error('Error fetching summary:', error));
  }

  // Handle Group Selection
  groupSelect.addEventListener('change', (e) => {
    const groupId = e.target.value;
    if (!groupId) {
      membersTable.innerHTML = '';
      expensesTable.innerHTML = '';
      summaryTable.innerHTML = '';
      return;
    }
    loadMembers(groupId);
    loadExpenses(groupId);
    loadSummary(groupId);
  });
});