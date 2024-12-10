export const getYearsBetweenDates = (registeredDate: Date): number[] => {
  const startYear = new Date(registeredDate).getFullYear();
  const currentYear = new Date().getFullYear();
  const years: number[] = [];
  for (let year = startYear; year <= currentYear; year++) {
    years.push(year);
  }
  return years;
};
